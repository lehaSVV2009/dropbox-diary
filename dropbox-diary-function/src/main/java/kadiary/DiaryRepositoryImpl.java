package kadiary;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

public class DiaryRepositoryImpl implements DiaryRepository {

  private final DbxClientV2 client;
  private final String diaryPath;

  public DiaryRepositoryImpl(AppContext appContext) {
    DbxRequestConfig config = new DbxRequestConfig(appContext.getDropboxClientId());
    this.client = new DbxClientV2(config, appContext.getDropboxClientSecret());
    this.diaryPath = appContext.getDropboxDiaryPath();
  }

  /**
   * Get diary text from
   */
  @Override
  public String read() throws DbxException {
    try (DbxDownloader<FileMetadata> downloader = client.files().download(diaryPath)) {
      return IOUtil.toUtf8String(downloader.getInputStream());
    } catch (IOUtil.ReadException | CharacterCodingException e) {
      throw new DbxException("Diary reading failed! " + e.getMessage());
    }
  }

  /**
   * Upload text to dropbox file
   */
  @Override
  public void write(String diary) throws IOException, DbxException {
    try (InputStream in = new ByteArrayInputStream(diary.getBytes(StandardCharsets.UTF_8))) {
      client.files().uploadBuilder(diaryPath).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
    } catch (JsonProcessingException e) {
      throw new DbxException("Failed uploading text to diary.yml! " + e.getMessage());
    }
  }
}
