package kadiary;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class Response {

  private static final String DROPBOX_ERROR_MESSAGE =
      "Error. Failed to update file in Dropbox";
  private static final String INVALID_REQUEST_MESSAGE =
      "Invalid format. The request should send note like `POST [{ \"text\": \"Bla bla\", \"date\": \"2018-05-21T17:48:16.667Z\"}]`";

  private final int statusCode;
  private final Object body;
  @Singular
  private final Map<String, String> headers;

  public static Response created(List<Event> newEvents) {
    return Response.builder()
        .statusCode(201)
        .body(newEvents)
        .header("Content-Type", "application/json")
        .build();
  }

  public static Response dropboxError() {
    return Response.builder()
        .statusCode(500)
        .body("{\"message\": \"" + DROPBOX_ERROR_MESSAGE + "\"}")
        .header("Content-Type", "application/json")
        .build();
  }

  public static Response badRequest() {
    return Response.builder()
        .statusCode(400)
        .body("{\"message\": \"" + INVALID_REQUEST_MESSAGE + "\"}")
        .header("Content-Type", "application/json")
        .build();
  }

}
