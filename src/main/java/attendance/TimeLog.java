package attendance;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeLog {

  public TimeLog(LocalDateTime dateTime) {
    if(dateTime.toLocalTime().isBefore(LocalTime.of(8,0)) || dateTime.toLocalTime().isAfter(LocalTime.of(23,0))) {
      throw new IllegalArgumentException("캠퍼스 운영 시간이 아닙니다");
    }
  }
}
