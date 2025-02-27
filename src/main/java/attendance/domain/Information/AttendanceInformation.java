package attendance.domain.Information;

import attendance.domain.crew.AttendanceStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceInformation {

  private final LocalDate date;
  private final LocalTime time;
  private final AttendanceStatus attendanceStatus;

  public AttendanceInformation(LocalDateTime dateTime, AttendanceStatus attendanceStatus) {
    this.date = dateTime.toLocalDate();
    this.time = dateTime.toLocalTime();
    this.attendanceStatus = attendanceStatus;
  }

  public AttendanceInformation(LocalDate date, LocalTime time, AttendanceStatus attendanceStatus) {
    this.date = date;
    this.time = time;
    this.attendanceStatus = attendanceStatus;
  }

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getTime() {
    return time;
  }

  public AttendanceStatus getAttendanceStatus() {
    return attendanceStatus;
  }

}
