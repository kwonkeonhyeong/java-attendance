package attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceLogResponse {

  private final LocalDate date;
  private final LocalTime time;
  private final String attendanceStatus;

  public AttendanceLogResponse(LocalDateTime dateTime, String attendanceStatus) {
    this.date = dateTime.toLocalDate();
    this.time = dateTime.toLocalTime();
    this.attendanceStatus = attendanceStatus;
  }

  public AttendanceLogResponse(LocalDate date, LocalTime time, String attendanceStatus) {
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

  public String getAttendanceStatus() {
    return attendanceStatus;
  }
}
