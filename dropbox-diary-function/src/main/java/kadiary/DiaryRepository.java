package kadiary;

import com.dropbox.core.DbxException;

import java.io.IOException;

public interface DiaryRepository {
  /**
   * Get diary text from
   */
  String read() throws DbxException;

  /**
   * Upload text to dropbox file
   */
  void write(String diary) throws IOException, DbxException;
}
