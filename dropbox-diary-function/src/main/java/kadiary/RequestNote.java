package kadiary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestNote {
  private final LocalDateTime date = LocalDateTime.now();
  private String note;
}
