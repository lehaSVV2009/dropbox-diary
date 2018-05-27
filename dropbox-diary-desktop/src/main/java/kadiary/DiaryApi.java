package kadiary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.List;

public interface DiaryApi {
  @POST("/v1")
  Call<FunctionResponse> createEvents(@Body List<Event> events, @Header("x-api-key") String apiKey);
}
