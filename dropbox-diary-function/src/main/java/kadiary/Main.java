package kadiary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;
import lombok.val;

public class Main implements RequestStreamHandler {

  private static final String ERROR_MESSAGE =
      "Invalid format. The request should send note like `POST { \"note\": \"Bla bla\"}`";

  private final ObjectMapper objectMapper =
      new ObjectMapper()
          // allow to return "2014-12-20T02:30" instead of { date: "12", month: "December", ... }
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class RequestNote {
    private final LocalDateTime date = LocalDateTime.now();
    private String note;
  }

  @Getter
  @Builder
  public static class Response {
    private final int statusCode;
    private final Object body;
    @Singular
    private final Map<String, String> headers;
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    try (val input = inputStream;
         val output = outputStream) {

      // Read http request in format { "note": "123" }
      val requestNote = objectMapper.readValue(input, RequestNote.class);
      context.getLogger().log("Request: " + requestNote);

      // Handle request and response
      val response = execute(requestNote);

      // Write http response in format { "note": "123" }
      context.getLogger().log("Response: " + response);
      objectMapper.writeValue(output, response);

    } catch (JsonParseException | JsonMappingException e) {
      objectMapper.writeValue(outputStream, invalid());
    }
  }

  private Response execute(RequestNote requestNote) {
    // Return HTTP 400 if response format is invalid
    if (requestNote == null || requestNote.getNote() == null) {
      return invalid();
    }

    // TODO
    // Save request in dropbox

    // Respond with successfully saved request
    return created(requestNote);
  }

  private static Response created(RequestNote note) {
    return Response.builder()
        .statusCode(201)
        .body(note)
        .header("Content-Type", "application/json")
        .build();
  }

  private static Response invalid() {
    return Response.builder()
        .statusCode(400)
        .body("{\"message\": \"" + ERROR_MESSAGE + "\"}")
        .header("Content-Type", "application/json")
        .build();
  }
}