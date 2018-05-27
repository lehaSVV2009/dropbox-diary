package kadiary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Diary {

  private List<Event> events;

  public void addEvents(List<Event> events) {
    this.events.addAll(events);
  }
}
