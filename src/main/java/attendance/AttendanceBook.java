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

  public LinkedHashMap<LocalDateTime, AttendanceStatus> search(String nickname,
      LocalDate searchDate) {
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

  public Map<AttendanceStatus, Integer> calculateAttendanceResult(String nickname,
      LocalDate searchDate) {
    Crew crew = new Crew(nickname);
    validateExistentCrew(crew);
    AttendanceRecords attendanceRecords = attendanceBook.get(crew);
    AttendanceEvaluator attendanceEvaluator = new AttendanceEvaluator();
    return attendanceEvaluator.calculateAttendanceResult(
        attendanceRecords.searchRecords(searchDate));
  }

  public Map<Crew, ManagementStatus> checkManagementCrews(LocalDate searchDate) {
    return sortedManagementStatus(searchDate).entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> ManagementStatus.from(applyAbsenceCountPolicy(entry.getValue()))
        ));
  }

  private Map<Crew, Map<AttendanceStatus, Integer>> sortedManagementStatus(LocalDate searchDate) {
    return convertAttendanceRecordsToResult(searchDate).entrySet().stream()
        .sorted(
            Comparator.comparing(
                    (Map.Entry<Crew, Map<AttendanceStatus, Integer>> entry) -> applyAbsenceCountPolicy(
                        entry.getValue()))
                .thenComparing(entry -> entry.getKey().getName())
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
  }

  private Map<Crew, Map<AttendanceStatus, Integer>> convertAttendanceRecordsToResult(
      LocalDate searchDate) {
    return attendanceBook.entrySet().stream()
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry ->
                    calculateAttendanceResult(entry.getKey().getName(), searchDate)
            )
        );
  }

  private int applyAbsenceCountPolicy(Map<AttendanceStatus, Integer> result) {
    return result.get(AttendanceStatus.ABSENCE) + (result.get(AttendanceStatus.LATE) / 3);
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
