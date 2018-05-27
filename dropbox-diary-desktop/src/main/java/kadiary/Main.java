package kadiary;

import static java.util.Collections.singletonList;
import static kadiary.BuildConfig.DEFAULT_API_KEY;
import static kadiary.BuildConfig.DEFAULT_API_URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

  private static final String API_URL = System.getProperty("api-url", DEFAULT_API_URL);
  private static final String API_KEY = System.getProperty("aip-key", DEFAULT_API_KEY);

  public static void main(String args[]) throws Exception {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    TextInputDialog dialog = new TextInputDialog("no time...");
    dialog.setTitle("Kadiary");
    dialog.setHeaderText(null);
    dialog.setContentText("What did you do yesterday?");
    dialog.getEditor().setMinWidth(500);

    dialog.showAndWait().ifPresent(message -> {
      try {
        createEvent(message);
      } catch (Throwable e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
      }
    });

    stop();
  }

  private static void createEvent(String message) throws IOException, RuntimeException {
    // Create Diary API client
    // gradle-build-config plugin is used to retrieve api url and api key
    DiaryApi api = new Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(JacksonConverterFactory.create(
            // allow to send "2014-12-20T02:30" instead of { date: "20", month: "December", ... }
            new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)))
        .client(
            // not fail after 5 seconds read waiting
            new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build())
        .build()
        .create(DiaryApi.class);

    // Build diary event model
    Event event = new Event(message, ZonedDateTime.now());

    // Save diary event by API
    Response<FunctionResponse> response;
    try {
      response = api.createEvents(singletonList(event), API_KEY).execute();
    } catch (Throwable throwable) {
      throw new RuntimeException("Network error occurred", throwable);
    }

    // Fail if response and its body are not successful
    if (!response.isSuccessful()) {
      throw new RuntimeException("Response is not successful. " + response.code());
    }
    FunctionResponse functionResponse = response.body();
    if (functionResponse == null) {
      throw new RuntimeException("Response doesn't contain body. " + response.code());
    }
    if (functionResponse.getBody() == null || !(functionResponse.getBody() instanceof List)) {
      throw new RuntimeException("Function response body is invalid. " + functionResponse);
    }
  }
}