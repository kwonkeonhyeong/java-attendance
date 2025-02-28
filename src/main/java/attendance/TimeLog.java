package attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeLog {

  private static final LocalTime CAMPUS_OPEN_TIME = LocalTime.of(8, 0);
  private static final LocalTime CAMPUS_CLOSE_TIME = LocalTime.of(23, 0);
  private static final String CAMPUS_CLOSED_MESSAGE = "캠퍼스 운영 시간이 아닙니다";

  private final LocalDate date;
  private final LocalTime time;

  public TimeLog(LocalDateTime dateTime) {
    validateCampusOperatingTime(dateTime.toLocalTime());
    this.date = dateTime.toLocalDate();
    this.time = dateTime.toLocalTime();
  }

  private static void validateCampusOperatingTime(LocalTime time) {
    if(time.isBefore(CAMPUS_OPEN_TIME) || time.isAfter(CAMPUS_CLOSE_TIME)) {
      throw new IllegalArgumentException(CAMPUS_CLOSED_MESSAGE);
    }
  }
}
