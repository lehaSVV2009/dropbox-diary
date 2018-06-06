package kadiary;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;

import com.dropbox.core.DbxException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
public class DiaryService {

  private final DiaryRepository diaryRepository;
  private final ObjectMapper yamlObjectMapper =
      new ObjectMapper(new YAMLFactory().disable(WRITE_DOC_START_MARKER))
          // allow to return "2018-06-01T22:56:01.46Z" instead of { date: "1", month: "June", ... }
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public void addEvents(List<Event> newEvents) throws IOException, DbxException {
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

    // Get diary text
    String diary = diaryRepository.read();

    // Check that diary is valid
    if (diary == null || !diary.startsWith("---\nevents")) {
      throw new DbxException("Diary is in incorrect format! ");
    }

    // Add events to diary text
    diary += yamlObjectMapper.writeValueAsString(newEvents);

    // Save new diary text
    diaryRepository.write(diary);
  }

}
