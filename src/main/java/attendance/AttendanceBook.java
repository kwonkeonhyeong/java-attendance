package attendance;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AttendanceBook {

  private final Map<Crew, AttendanceRecords> attendanceBook;

  public AttendanceBook(Map<Crew, AttendanceRecords> attendanceBook) {
    this.attendanceBook = attendanceBook;
  }

  public ExistentAttendanceRecord checkAttendance(String nickname, LocalDateTime dateTime) {
    Crew crew = new Crew(nickname);
    validateExistentCrew(crew);
    return attendanceBook.get(crew).save(new ExistentAttendanceRecord(dateTime));
  }

  public Entry<AttendanceRecord, AttendanceRecord> modifyAttendanceRecord(String nickname, LocalDateTime updateDateTime) {
    Crew crew = new Crew(nickname);
    validateExistentCrew(crew);
    AttendanceRecords attendanceRecords = attendanceBook.get(crew);
    ExistentAttendanceRecord updateAttendanceRecord = new ExistentAttendanceRecord(updateDateTime);
    AttendanceRecord attendanceRecord = attendanceRecords.modifyAttendanceRecord(updateAttendanceRecord);
    return Map.entry(attendanceRecord, updateAttendanceRecord);
  }

  private void validateExistentCrew(Crew crew) {
    if (!attendanceBook.containsKey(crew)) {
      throw new IllegalArgumentException("등록되지 않은 크루입니다");
    }
  }

  public LinkedHashMap<LocalDateTime, AttendanceStatus> search(String nickname) {
    return null;
  }
}
