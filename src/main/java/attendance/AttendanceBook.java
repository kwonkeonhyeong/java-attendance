package attendance;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AttendanceBook {

  private final Map<Crew, AttendanceRecords> attendanceBook = new HashMap<>();

  public AttendanceRecord checkAttendance (String nickname, LocalDateTime dateTime) {
    Crew crew = new Crew(nickname);
    if(!attendanceBook.containsKey(crew)) {
      throw new IllegalArgumentException("등록되지 않은 크루입니다");
    }
    return attendanceBook.get(crew).save(new AttendanceRecord(dateTime));
  }
}
