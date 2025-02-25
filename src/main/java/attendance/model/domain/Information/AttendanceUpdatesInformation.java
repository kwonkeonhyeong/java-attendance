package attendance.model.domain.Information;

import attendance.model.domain.crew.TimeLog;
import java.time.LocalDateTime;

public class AttendanceUpdatesInformation {

  private final LocalDateTime before;
  private final LocalDateTime after;

  private final String beforeStatus;
  private final String afterStatus;

  private AttendanceUpdatesInformation(LocalDateTime before, LocalDateTime after, String beforeStatus,
      String afterStatus) {
    this.before = before;
    this.after = after;
    this.beforeStatus = beforeStatus;
    this.afterStatus = afterStatus;
  }

  public static AttendanceUpdatesInformation of(TimeLog before, TimeLog after) {
    return new AttendanceUpdatesInformation(
        before.getDateTime(),
        after.getDateTime(),
        before.getAttendanceStatus().getName(),
        after.getAttendanceStatus().getName()
    );
  }

  public LocalDateTime getBefore() {
    return before;
  }

  public LocalDateTime getAfter() {
    return after;
  }

  public String getBeforeStatus() {
    return beforeStatus;
  }

  public String getAfterStatus() {
    return afterStatus;
  }

}
