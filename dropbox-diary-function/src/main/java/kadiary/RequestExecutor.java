package kadiary;

import static kadiary.Response.badRequest;
import static kadiary.Response.created;
import static kadiary.Response.dropboxError;

import com.dropbox.core.DbxException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class RequestExecutor implements BiFunction<List<Event>, AppContext, Response> {

  private final DiaryService diaryService = new DiaryService();

  @Override
  public Response apply(List<Event> newEvents, AppContext appContext) {
    // Return HTTP 400 if response format is invalid
    if (newEvents == null || newEvents.isEmpty()) {
      return badRequest();
    }

    try {
      diaryService.addEvents(newEvents, appContext);
    } catch (DbxException | IOException e) {
      // Return HTTP 500 if dropbox update failed
      return dropboxError();
    }

    // Return HTTP 201 with successfully saved note
    return created(newEvents);
  }
}
