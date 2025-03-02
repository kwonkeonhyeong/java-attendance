package attendance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class AbsenceRecord implements AttendanceRecord{

  private static final String NON_OPERATING_DAY_MESSAGE = "주말 또는 공휴일에는 운영하지 않습니다.";

  private final LocalDate date;

  public AbsenceRecord(LocalDate date) {
    validateCampusOperatingDate(date);
    this.date = date;
  }

  private void validateCampusOperatingDate(LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
      throw new IllegalArgumentException(NON_OPERATING_DAY_MESSAGE);
    }
    if(Holiday.contains(date)) {
      throw  new IllegalArgumentException(NON_OPERATING_DAY_MESSAGE);
    }
  }

  public ExistentAttendanceRecord modifyTime(LocalTime time) {
    return new ExistentAttendanceRecord(LocalDateTime.of(date,time));
  }

  @Override
  public boolean isExists() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbsenceRecord attendanceRecord = (AbsenceRecord) o;
    return Objects.equals(date, attendanceRecord.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date);
  }

}
