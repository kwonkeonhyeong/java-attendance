package attendance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class TimeLog {

  private static final LocalTime CAMPUS_OPEN_TIME = LocalTime.of(8, 0);
  private static final LocalTime CAMPUS_CLOSE_TIME = LocalTime.of(23, 0);
  private static final String CAMPUS_CLOSED_MESSAGE = "캠퍼스 운영 시간이 아닙니다";
  private static final String NON_OPERATING_DAY_MESSAGE = "주말 또는 공휴일에는 운영하지 않습니다.";

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
      throw new IllegalArgumentException(NON_OPERATING_DAY_MESSAGE);
    }
    if(Holiday.contains(dateTime.toLocalDate())) {
      throw  new IllegalArgumentException(NON_OPERATING_DAY_MESSAGE);
    }
  }

  public LocalDateTime getDateTime() {
    return LocalDateTime.of(date,time);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeLog timeLog = (TimeLog) o;
    return Objects.equals(date, timeLog.date) && Objects.equals(time,
        timeLog.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, time);
  }
}
