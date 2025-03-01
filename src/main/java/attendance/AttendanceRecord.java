package attendance;

import java.time.LocalDateTime;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttendanceRecord that = (AttendanceRecord) o;
    return Objects.equals(crew, that.crew) && Objects.equals(timeLog,
        that.timeLog);
  }

  @Override
  public int hashCode() {
    return Objects.hash(crew, timeLog);
  }

}
