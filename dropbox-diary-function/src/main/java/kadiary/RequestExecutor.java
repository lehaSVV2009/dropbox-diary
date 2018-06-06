package kadiary;

import static kadiary.FunctionResponse.badRequest;
import static kadiary.FunctionResponse.created;
import static kadiary.FunctionResponse.dropboxError;

import com.dropbox.core.DbxException;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class RequestExecutor implements Function<List<Event>, FunctionResponse> {

  private final DiaryService diaryService;

  public RequestExecutor(AppContext appContext) {
    this.diaryService = new DiaryService(new DiaryRepositoryImpl(appContext));
  }

  @Override
  public FunctionResponse apply(List<Event> newEvents) {
    // Return HTTP 400 if response format is invalid
    if (newEvents == null || newEvents.isEmpty()) {
      return badRequest();
    }

    try {
      diaryService.addEvents(newEvents);
    } catch (DbxException | IOException e) {
      // Return HTTP 500 if dropbox update failed
      return dropboxError();
    }

    // Return HTTP 201 with successfully saved note
    return created(newEvents);
  }
}
