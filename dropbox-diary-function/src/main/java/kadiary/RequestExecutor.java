package kadiary;

import static kadiary.Dropboxer.updateInDropbox;
import static kadiary.Response.badRequest;
import static kadiary.Response.created;
import static kadiary.Response.dropboxError;

import com.dropbox.core.DbxException;

import java.io.IOException;
import java.util.function.BiFunction;

public class RequestExecutor implements BiFunction<RequestNote, AppConfig, Response> {

  @Override
  public Response apply(RequestNote requestNote, AppConfig appConfig) {
    // Return HTTP 400 if response format is invalid
    if (requestNote == null || requestNote.getNote() == null) {
      return badRequest();
    }

    try {
      updateInDropbox(
          requestNote.getNote(),
          requestNote.getDate(),
          appConfig.getDropboxClientId(),
          appConfig.getDropboxClientSecret(),
          null
      );
    } catch (DbxException | IOException e) {
      // Return HTTP 500 if dropbox update failed
      return dropboxError();
    }

    // Return HTTP 201 with successfully saved note
    return created(requestNote);
  }
}
