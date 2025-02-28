package attendance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class TimeLog {

  private static final LocalTime CAMPUS_OPEN_TIME = LocalTime.of(8, 0);
  private static final LocalTime CAMPUS_CLOSE_TIME = LocalTime.of(23, 0);
  private static final String CAMPUS_CLOSED_MESSAGE = "캠퍼스 운영 시간이 아닙니다";

  private final LocalDate date;
  private final LocalTime time;

  public TimeLog(LocalDateTime dateTime) {
    validateCampusOperatingDate(dateTime);
    validateCampusOperatingTime(dateTime.toLocalTime());
    this.date = dateTime.toLocalDate();
    this.time = dateTime.toLocalTime();
  }

  private void validateCampusOperatingTime(LocalTime time) {
    if(time.isBefore(CAMPUS_OPEN_TIME) || time.isAfter(CAMPUS_CLOSE_TIME)) {
      throw new IllegalArgumentException(CAMPUS_CLOSED_MESSAGE);
    }
  }

  private void validateCampusOperatingDate(LocalDateTime dateTime) {
    DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
    if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
      throw new IllegalArgumentException("주말 또는 공휴일에는 운영하지 않습니다.");
    }
    if(dateTime.getMonth() == Month.DECEMBER && dateTime.getDayOfMonth() == 25) {
      throw  new IllegalArgumentException("주말 또는 공휴일에는 운영하지 않습니다.");
    }
  }

}
