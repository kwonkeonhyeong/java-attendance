package attendance;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AttendanceRecordsTest {
  @DisplayName("출석_기록_저장_후_저장된_기록_반환")
  @Test
  void saveAttendanceRecordTest() {
    AttendanceRecords attendanceRecords = new AttendanceRecords();
    AttendanceRecord attendanceRecord = new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 0));
    AttendanceRecord savedAttendanceRecord = attendanceRecords.save(attendanceRecord);
    Assertions.assertThat(savedAttendanceRecord).isEqualTo(attendanceRecord);
  }
}
