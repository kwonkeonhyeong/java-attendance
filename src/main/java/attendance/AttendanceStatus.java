package attendance;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public enum AttendanceStatus {

  ATTENDANCE,
  LATE,
  ABSENCE;

  public static AttendanceStatus from(TimeLog timeLog) {
    LocalDateTime dateTime = timeLog.getDateTime();
    if (dateTime.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
      if (dateTime.toLocalTime().isBefore(LocalTime.of(13, 6))) {
        return ATTENDANCE;
      }
      if (dateTime.toLocalTime().isBefore(LocalTime.of(13, 31))) {
        return LATE;
      }
        return ABSENCE;
    }

    if (dateTime.toLocalTime().isBefore(LocalTime.of(10, 6))) {
      return ATTENDANCE;
    }
    if (dateTime.toLocalTime().isBefore(LocalTime.of(10, 31))) {
      return LATE;
    }
    return ABSENCE;
  }
}
