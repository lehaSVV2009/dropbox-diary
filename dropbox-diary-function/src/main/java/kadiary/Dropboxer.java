package kadiary;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Dropboxer {

  private static final String DEFAULT_DIARY_PATH = "/diary.xml";

  public static void updateInDropbox(
      String event,
      LocalDateTime date,
      String dropboxClientId,
      String dropboxAccessToken,
      String dropboxDiaryPath) throws IOException, DbxException {

    if (event == null) {
      throw new DbxException("Event and date can not be null");
    }
    if (date == null) {
      date = LocalDateTime.now();
    }

    // Create Dropbox client
    // gradle-build-config plugin is used to retrieve dropbox client id, token and path from system env
    DbxRequestConfig config = new DbxRequestConfig(dropboxClientId);
    DbxClientV2 client = new DbxClientV2(config, dropboxAccessToken);
    String diaryPath = dropboxDiaryPath == null ? DEFAULT_DIARY_PATH : dropboxDiaryPath;

    // Get diary file from Dropbox
    String diary;
    try (DbxDownloader<FileMetadata> downloader = client.files().download(diaryPath)) {
      diary = IOUtil.toUtf8String(downloader.getInputStream());
    }

    if (diary == null || !diary.endsWith("</diary>")) {
      throw new DbxException("Diary is in incorrect format!");
    }

    // Add new item to diary
    diary = diary.replace(
        "</diary>",
        "<event><date>" + date + "</date><text>" + event + "</text></event></diary>"
    );

    // Upload file to Dropbox
    try (InputStream in = new ByteArrayInputStream(diary.getBytes(StandardCharsets.UTF_8))) {
      client.files().uploadBuilder(diaryPath).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
    }
  }

}
