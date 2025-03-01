package attendance;

import java.time.LocalTime;

public enum AttendanceStatus {

  ATTENDANCE,
  LATE,
  ABSENCE;

  private static final LocalTime mondayAttendanceStartTime = LocalTime.of(13,0);
  private static final LocalTime weekdayAttendanceStartTime = LocalTime.of(10,0);

  private static final long ATTENDANCE_APPROVAL_MINUTE = 5;
  private static final long LATE_APPROVAL_MINUTE = 30;

  public static AttendanceStatus from(TimeLog timeLog) {
    if (timeLog.isMonday()) {
      return judgeAttendanceManagement(timeLog, mondayAttendanceStartTime);
    }
    return judgeAttendanceManagement(timeLog, weekdayAttendanceStartTime);
  }

  private static AttendanceStatus judgeAttendanceManagement(TimeLog timeLog, LocalTime startTime) {
    if (timeLog.isAttendance(startTime.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))) {
      return ATTENDANCE;
    }
    if (timeLog.isLate(startTime.plusMinutes(LATE_APPROVAL_MINUTE))) {
      return LATE;
    }
    return ABSENCE;
  }

}
