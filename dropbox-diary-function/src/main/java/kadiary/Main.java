package kadiary;

import static kadiary.FunctionResponse.badRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.val;

public class Main implements RequestStreamHandler {

  private static final String DROPBOX_CLIENT_ID = "DROPBOX_CLIENT_ID";
  private static final String DROPBOX_ACCESS_TOKEN = "DROPBOX_ACCESS_TOKEN";
  private static final String DROPBOX_DIARY_PATH = "DROPBOX_DIARY_PATH";

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

      // Read http request in format
      // [{ "text": "bla", "date": "2018-05-21T17:48:16.667Z" }, ...]
      List<Event> request = objectMapper.readValue(input, new TypeReference<List<Event>>() {});
      context.getLogger().log("Request: " + request);

      // Read all required env variables
      val env = System.getenv();
      val appConfig = AppContext.builder()
          .dropboxClientId(env.get(DROPBOX_CLIENT_ID))
          .dropboxClientSecret(env.get(DROPBOX_ACCESS_TOKEN))
          .dropboxDiaryPath(env.get(DROPBOX_DIARY_PATH))
          .build();
      context.getLogger().log("Config: " + appConfig);

      // Process request
      val response = requestExecutor.apply(request, appConfig);

      // Write http response in format { "note": "123" }
      context.getLogger().log("Response: " + response);
      objectMapper.writeValue(output, response);

    } catch (JsonParseException | JsonMappingException e) {
      objectMapper.writeValue(outputStream, badRequest());
    }
  }

}