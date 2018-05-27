package kadiary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {

  private static final String DROPBOX_ERROR_MESSAGE =
      "Error. Failed to update file in Dropbox";
  private static final String INVALID_REQUEST_MESSAGE =
      "Invalid format. The request should send note like `POST [{ \"text\": \"Bla bla\", \"date\": \"2018-05-21T17:48:16.667Z\"}]`";

  private int statusCode;
  private Object body;
  @Singular
  private Map<String, String> headers;

  public static FunctionResponse created(List<Event> newEvents) {
    return FunctionResponse.builder()
        .statusCode(201)
        .body(newEvents)
        .header("Content-Type", "application/json")
        .build();
  }

  public static FunctionResponse dropboxError() {
    return FunctionResponse.builder()
        .statusCode(500)
        .body("{\"message\": \"" + DROPBOX_ERROR_MESSAGE + "\"}")
        .header("Content-Type", "application/json")
        .build();
  }

  public static FunctionResponse badRequest() {
    return FunctionResponse.builder()
        .statusCode(400)
        .body("{\"message\": \"" + INVALID_REQUEST_MESSAGE + "\"}")
        .header("Content-Type", "application/json")
        .build();
  }

}
