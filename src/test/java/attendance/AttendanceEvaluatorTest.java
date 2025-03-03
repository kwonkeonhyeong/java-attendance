package attendance;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AttendanceEvaluatorTest {

  private final  AttendanceEvaluator attendanceEvaluator = new AttendanceEvaluator();
//  private final AttendanceBook attendanceBook = new AttendanceBook(
//      new HashMap<>(
//          Map.of(
//              new Crew("히포"), new AttendanceRecords()
//          )
//      )
//  );
  private final AttendanceRecords attendanceRecords = new AttendanceRecords();

  @DisplayName("출석_기록을_기준으로_출석_결과_반환")
  @Test
  void calculateAttendanceResultTest() {
    setRecords();
    List<AttendanceRecord> searchedAttendanceRecords = attendanceRecords.searchRecords(LocalDate.of(2025, 2, 11));
    Map<AttendanceStatus, Integer> attendanceResult = attendanceEvaluator.calculateAttendanceResult(searchedAttendanceRecords);
    assertThat(attendanceResult).isEqualTo(createExpectedAttendanceResult());
  }

  private void setRecords() {
    attendanceRecords.save(new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 3, 13, 6)));
    attendanceRecords.save(new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 4, 10, 5)));
    attendanceRecords.save(new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 5, 10, 31)));
    attendanceRecords.save(new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 6, 10, 13)));
    attendanceRecords.save(new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 7, 10, 5)));
    attendanceRecords.save(new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 10, 13, 5)));
  }

  private Map<AttendanceStatus, Integer> createExpectedAttendanceResult() {
    Map<AttendanceStatus, Integer> expected = new HashMap<>();
    expected.put(AttendanceStatus.ATTENDANCE, 3);
    expected.put(AttendanceStatus.LATE, 3);
    expected.put(AttendanceStatus.ABSENCE, 1);
    return expected;
  }
}
