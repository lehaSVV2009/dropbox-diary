package kadiary;

import static kadiary.BuildConfig.DEFAULT_DIARY_PATH;
import static kadiary.BuildConfig.DEFAULT_DROPBOX_ACCESS_TOKEN;
import static kadiary.BuildConfig.DEFAULT_DROPBOX_CLIENT_ID;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Main extends Application {

  private static final String DROPBOX_CLIENT_ID = System.getProperty("dropbox-client-id", DEFAULT_DROPBOX_CLIENT_ID);
  private static final String DROPBOX_ACCESS_TOKEN = System.getProperty("dropbox-access-token", DEFAULT_DROPBOX_ACCESS_TOKEN);
  private static final String DIARY_PATH = System.getProperty("diary-path", DEFAULT_DIARY_PATH);

  public static void main(String args[]) throws Exception {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    TextInputDialog dialog = new TextInputDialog("no time...");
    dialog.setTitle("Kadiary");
    dialog.setHeaderText(null);
    dialog.setContentText("What did you do yesterday?");
    dialog.getEditor().setMinWidth(500);

    dialog.showAndWait().ifPresent(event -> {
      try {
        Main.updateInDropBox(event);
      } catch (Exception e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
      }
    });

    stop();
  }

  private static void updateInDropBox(String event) throws IOException, DbxException {
    // Create Dropbox client
    // gradle-build-config plugin is used to retrieve dropbox client id, token and path from system env
    DbxRequestConfig config = new DbxRequestConfig(DROPBOX_CLIENT_ID);
    DbxClientV2 client = new DbxClientV2(config, DROPBOX_ACCESS_TOKEN);

    // Get diary file from Dropbox
    String diary;
    try (DbxDownloader<FileMetadata> downloader = client.files().download(DIARY_PATH)) {
      diary = IOUtil.toUtf8String(downloader.getInputStream());
    }

    if (diary == null || !diary.endsWith("</diary>")) {
      throw new DbxException("Diary is in incorrect format!");
    }

    // Add new item to diary
    diary = diary.replace(
        "</diary>",
        "<event><date>" + LocalDateTime.now() + "</date><text>" + event + "</text></event></diary>"
    );

    // Upload file to Dropbox
    try (InputStream in = new ByteArrayInputStream(diary.getBytes(StandardCharsets.UTF_8))) {
      client.files().uploadBuilder(DIARY_PATH).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
    }
  }
}