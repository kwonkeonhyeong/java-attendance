package attendance.domain.Information;

import attendance.domain.crew.AttendanceStatus;
import attendance.domain.crew.TimeLog;
import java.time.LocalDateTime;

public class AttendanceUpdatesInformation {

  private final LocalDateTime before;
  private final LocalDateTime after;

  private final AttendanceStatus beforeStatus;
  private final AttendanceStatus afterStatus;

  private AttendanceUpdatesInformation(LocalDateTime before, LocalDateTime after,
      AttendanceStatus beforeStatus,
      AttendanceStatus afterStatus) {
    this.before = before;
    this.after = after;
    this.beforeStatus = beforeStatus;
    this.afterStatus = afterStatus;
  }

  public static AttendanceUpdatesInformation of(TimeLog before, TimeLog after) {
    return new AttendanceUpdatesInformation(
        before.getDateTime(),
        after.getDateTime(),
        before.judgeAttendanceStatus(),
        after.judgeAttendanceStatus()
    );
  }

  public LocalDateTime getBefore() {
    return before;
  }

  public LocalDateTime getAfter() {
    return after;
  }

  public AttendanceStatus getBeforeStatus() {
    return beforeStatus;
  }

  public AttendanceStatus getAfterStatus() {
    return afterStatus;
  }

}
