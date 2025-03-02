package attendance;

import java.time.LocalDateTime;
import java.util.Map;

public class AttendanceBook {

  private final Map<Crew, AttendanceRecords> attendanceBook;

  public AttendanceBook(Map<Crew, AttendanceRecords> attendanceBook) {
    this.attendanceBook = attendanceBook;
  }

  public ExistentAttendanceRecord checkAttendance (String nickname, LocalDateTime dateTime) {
    Crew crew = new Crew(nickname);
    validateExistentCrew(crew);
    return attendanceBook.get(crew).save(new ExistentAttendanceRecord(dateTime));
  }

  private void validateExistentCrew(Crew crew) {
    if(!attendanceBook.containsKey(crew)) {
      throw new IllegalArgumentException("등록되지 않은 크루입니다");
    }
  }
}
