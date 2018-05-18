package kadiary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main implements RequestStreamHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  // TODO try to use lombok
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Request {
    private String note;

    public String getNote() {
      return note;
    }

    public void setNote(String note) {
      this.note = note;
    }
  }

  // TODO try to use lombok builder
  public static class Response {
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;

    public Response(int statusCode, String body, Map<String, String> headers) {
      this.statusCode = statusCode;
      this.body = body;
      this.headers = headers;
    }

    public int getStatusCode() {
      return statusCode;
    }

    public String getBody() {
      return body;
    }

    public Map<String, String> getHeaders() {
      return headers;
    }
  }

  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    // TODO probably wrap input and output by try with resources
    Request request = objectMapper.readValue(input, Request.class);

    // TODO do some dropbox stuff

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    Response response = new Response(201, request.toString(), headers);
    objectMapper.writeValue(output, response);
  }
}