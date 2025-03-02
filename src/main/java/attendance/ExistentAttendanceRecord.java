package attendance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class ExistentAttendanceRecord implements AttendanceRecord{

  private static final LocalTime CAMPUS_OPEN_TIME = LocalTime.of(8, 0);
  private static final LocalTime CAMPUS_CLOSE_TIME = LocalTime.of(23, 0);
  private static final String CAMPUS_CLOSED_MESSAGE = "캠퍼스 운영 시간이 아닙니다";
  private static final String NON_OPERATING_DAY_MESSAGE = "주말 또는 공휴일에는 운영하지 않습니다.";

  private final LocalDate date;
  private final LocalTime time;

  public ExistentAttendanceRecord(LocalDateTime dateTime) {
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

  public ExistentAttendanceRecord modifyTime(LocalTime time) {
    return new ExistentAttendanceRecord(LocalDateTime.of(date,time));
  }

  public boolean isMonday() {
    return date.getDayOfWeek().equals(DayOfWeek.MONDAY);
  }

  public boolean isAttendance(LocalTime attendanceDeadline) {
    return time.isBefore(attendanceDeadline) || time.equals(attendanceDeadline);
  }

  public boolean isLate(LocalTime lateDeadline) {
    return time.isBefore(lateDeadline) || time.equals(lateDeadline);
  }

  public LocalDateTime getDateTime() {
    return LocalDateTime.of(date, time);
  }

  @Override
  public LocalDateTime getRecord() {
    return getDateTime();
  }

  @Override
  public boolean isExists() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExistentAttendanceRecord existentAttendanceRecord = (ExistentAttendanceRecord) o;
    return Objects.equals(date, existentAttendanceRecord.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date);
  }

}
