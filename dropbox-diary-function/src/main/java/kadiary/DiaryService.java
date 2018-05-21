package kadiary;

import static kadiary.AppContext.DEFAULT_DIARY_PATH;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.List;

public class DiaryService {

  private ObjectMapper yamlObjectMapper =
      new ObjectMapper(new YAMLFactory())
          // allow to return "2014-12-20T02:30" instead of { date: "12", month: "December", ... }
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public void addEvents(List<Event> newEvents, AppContext appContext) throws IOException, DbxException {
    // Validate new events
    if (newEvents == null ||
        newEvents.isEmpty() ||
        newEvents.stream().anyMatch(event -> event.getText() == null)) {
      throw new DbxException("Events are not fulfilled! Text is required");
    }

    // Fill dates if they are empty in the event
    newEvents.forEach(event -> {
      if (event.getDate() == null) {
        event.setDate(ZonedDateTime.now());
      }
    });

    // Create Dropbox client
    DbxRequestConfig config = new DbxRequestConfig(appContext.getDropboxClientId());
    DbxClientV2 client = new DbxClientV2(config, appContext.getDropboxClientSecret());
    String diaryPath = appContext.getDropboxDiaryPath() == null ? DEFAULT_DIARY_PATH : appContext.getDropboxDiaryPath();

    // Get diary file from Dropbox
    Diary diary;
    try (DbxDownloader<FileMetadata> downloader = client.files().download(diaryPath)) {
      diary = yamlObjectMapper.readValue(downloader.getInputStream(), Diary.class);
    } catch (JsonParseException | JsonMappingException e) {
      throw new DbxException("Diary is in incorrect format! " + e.getMessage());
    }

    // Add new events to the end of diary
    diary.addEvents(newEvents);

    // Upload file to Dropbox
    try (InputStream in = new ByteArrayInputStream(yamlObjectMapper.writeValueAsString(diary).getBytes(StandardCharsets.UTF_8))) {
      client.files().uploadBuilder(diaryPath).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
    } catch (JsonProcessingException e) {
      throw new DbxException("Failed converting Diary.class to diary.yml! " + e.getMessage());
    }
  }

}
