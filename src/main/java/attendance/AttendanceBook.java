package attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class AttendanceBook {

  private final Map<Crew, AttendanceRecords> attendanceBook;

  public AttendanceBook(Map<Crew, AttendanceRecords> attendanceBook) {
    this.attendanceBook = attendanceBook;
  }

  public ExistentAttendanceRecord check(String nickname, LocalDateTime dateTime) {
    Crew crew = new Crew(nickname);
    validateExistentCrew(crew);
    return attendanceBook.get(crew).save(new ExistentAttendanceRecord(dateTime));
  }

  public Entry<AttendanceRecord, AttendanceRecord> modify(String nickname,
      LocalDateTime updateDateTime) {
    Crew crew = new Crew(nickname);
    validateExistentCrew(crew);
    AttendanceRecords attendanceRecords = attendanceBook.get(crew);
    ExistentAttendanceRecord updateAttendanceRecord = new ExistentAttendanceRecord(updateDateTime);
    AttendanceRecord attendanceRecord = attendanceRecords.modifyAttendanceRecord(
        updateAttendanceRecord);
    return Map.entry(attendanceRecord, updateAttendanceRecord);
  }

  public LinkedHashMap<LocalDateTime, AttendanceStatus> search(String nickname, LocalDate searchDate) {
    Crew crew = new Crew(nickname);
    validateExistentCrew(crew);
    List<AttendanceRecord> attendanceRecords = attendanceBook.get(crew).searchRecords(searchDate);
    return sorted(attendanceRecords).stream()
        .collect(Collectors.toMap(
            AttendanceRecord::getRecord,
            AttendanceStatus::from,
            (existing, replacement) -> existing,
            LinkedHashMap::new
        ));
  }

  private List<AttendanceRecord> sorted(List<AttendanceRecord> attendanceRecords) {
    return attendanceRecords.stream()
        .sorted(Comparator.comparing(AttendanceRecord::getRecord))
        .toList();
  }

  private void validateExistentCrew(Crew crew) {
    if (!attendanceBook.containsKey(crew)) {
      throw new IllegalArgumentException("등록되지 않은 크루입니다");
    }
  }
}
