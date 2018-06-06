package kadiary;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class AppContext {

  public static final String DEFAULT_DIARY_PATH = "/diary.yml";

  @NonNull
  private String dropboxClientId;
  @NonNull
  private String dropboxClientSecret;
  private String dropboxDiaryPath;

  public String getDropboxDiaryPath() {
    return dropboxDiaryPath == null ? DEFAULT_DIARY_PATH : dropboxDiaryPath;
  }
}
