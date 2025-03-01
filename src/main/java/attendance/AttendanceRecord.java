package attendance;

import java.time.LocalDateTime;

public class AttendanceRecord {

  private final Crew crew;
  private final TimeLog timeLog;

  public AttendanceRecord(String nickname, LocalDateTime attendanceTime) {
    this.crew = new Crew(nickname);
    this.timeLog = new TimeLog(attendanceTime);
  }

  public Crew getNickname() {
    return crew;
  }

  public TimeLog getTimeLog() {
    return timeLog;
  }
}
