package kadiary;

import static kadiary.Response.badRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.val;

public class Main implements RequestStreamHandler {

  private static final String DROPBOX_CLIENT_ID = "DROPBOX_CLIENT_ID";
  private static final String DROPBOX_ACCESS_TOKEN = "DROPBOX_ACCESS_TOKEN";

  private final RequestExecutor requestExecutor = new RequestExecutor();
  private final ObjectMapper objectMapper =
      new ObjectMapper()
          // allow to return "2014-12-20T02:30" instead of { date: "12", month: "December", ... }
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    try (val input = inputStream;
         val output = outputStream) {

      // Read http request in format { "note": "123" }
      val requestNote = objectMapper.readValue(input, RequestNote.class);
      context.getLogger().log("Request: " + requestNote);

      // Read all required env variables
      val appConfig = new AppConfig(
          System.getenv(DROPBOX_CLIENT_ID),
          System.getenv(DROPBOX_ACCESS_TOKEN)
      );
      context.getLogger().log("Config: " + appConfig);

      // Process request
      val response = requestExecutor.apply(requestNote, appConfig);

      // Write http response in format { "note": "123" }
      context.getLogger().log("Response: " + response);
      objectMapper.writeValue(output, response);

    } catch (JsonParseException | JsonMappingException e) {
      objectMapper.writeValue(outputStream, badRequest());
    }
  }

}