package attendance.model.domain.crew;

import attendance.dto.AttendanceLogResponse;
import attendance.model.domain.calender.Calender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

public class TimeLog implements Comparator<TimeLog> {

  private static final LocalTime MONDAY_LATE_TIME = LocalTime.of(13, 5);
  private static final LocalTime MONDAY_ABSENCE_TIME = LocalTime.of(13, 30);
  private static final LocalTime WEEKDAY_LATE_TIME = LocalTime.of(10, 5);
  private static final LocalTime WEEKDAY_ABSENCE_TIME = LocalTime.of(10, 30);
  private static final LocalTime CAMPUS_OPEN_TIME = LocalTime.of(8, 0, 0);
  private static final LocalTime CAMPUS_CLOSE_TIME = LocalTime.of(23, 0, 0);

  private static final String NON_BUSINESS_DAY_MESSAGE = "주말 또는 공휴일에는 출석 기록을 생성할 수 없습니다";
  private static final String NON_BUSINESS_HOURS_MESSAGE_FORMAT = "캠퍼스 운영시간이 아닙니다 (운영시간 매일 %02d:%02d~%02d:%02d)";
  private static final String NON_BUSINESS_HOURS_MESSAGE = String.format(
      NON_BUSINESS_HOURS_MESSAGE_FORMAT, CAMPUS_OPEN_TIME.getHour(), CAMPUS_OPEN_TIME.getMinute(),
      CAMPUS_CLOSE_TIME.getHour(), CAMPUS_CLOSE_TIME.getMinute());

  private final LocalDate date;
  private final LocalTime time;

  private TimeLog(LocalDate date, LocalTime time) {
    validateUnavailableTime(time);
    validateUnavailableDate(date);
    this.date = date;
    this.time = time;
  }

  public static TimeLog of(LocalDate localDate, LocalTime localTime) {
    return new TimeLog(localDate, localTime);
  }

  public static TimeLog from(LocalDateTime dateTime) {
    return new TimeLog(dateTime.toLocalDate(), dateTime.toLocalTime());
  }

  public boolean isSame(LocalDate date) {
    return this.date.equals(date);
  }

  public LocalDateTime getDateTime() {
    return LocalDateTime.of(date, time);
  }

  public AttendanceStatus getAttendanceStatus() {
    if (isAbsence(getDateTime())) {
      return AttendanceStatus.ABSENCE;
    }
    if (isLate(getDateTime())) {
      return AttendanceStatus.LATE;
    }
    return AttendanceStatus.ATTENDANCE;
  }

  public AttendanceLogResponse getAttendanceLogResponse() {
    return new AttendanceLogResponse(date, time, getAttendanceStatus().getName());
  }

  @Override
  public int compare(TimeLog o1, TimeLog o2) {
    return o1.date.compareTo(o2.date);
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

  @Override
  public String toString() {
    return "TimeLog{" +
        "date=" + date +
        ", time=" + time +
        '}';
  }

  private void validateUnavailableTime(LocalTime time) {
    if (time.isBefore(CAMPUS_OPEN_TIME) || time.isAfter(CAMPUS_CLOSE_TIME)) {
      throw new IllegalArgumentException(NON_BUSINESS_HOURS_MESSAGE);
    }
  }

  private void validateUnavailableDate(LocalDate date) {
    if (Calender.HOLIDAY.isContainDate(date) || Calender.WEEKEND.isContainDate(date)) {
      throw new IllegalArgumentException(NON_BUSINESS_DAY_MESSAGE);
    }
  }

  private boolean isAbsence(LocalDateTime dateTime) {
    if (Calender.isMonday(dateTime.toLocalDate())) {
      return isTimeAfter(dateTime.toLocalTime(), MONDAY_ABSENCE_TIME);
    }
    return isTimeAfter(dateTime.toLocalTime(), WEEKDAY_ABSENCE_TIME);
  }

  private boolean isLate(LocalDateTime dateTime) {
    if (Calender.isMonday(dateTime.toLocalDate())) {
      return isTimeBetween(dateTime.toLocalTime(), MONDAY_LATE_TIME,
          MONDAY_ABSENCE_TIME.plusMinutes(1));
    }
    return isTimeBetween(dateTime.toLocalTime(), WEEKDAY_LATE_TIME,
        WEEKDAY_ABSENCE_TIME.plusMinutes(1));
  }

  private boolean isTimeAfter(LocalTime time, LocalTime baseTime) {
    return time.isAfter(baseTime);
  }

  private boolean isTimeBetween(LocalTime time, LocalTime startTime, LocalTime endTime) {
    return time.isAfter(startTime) && time.isBefore(endTime);
  }

}
