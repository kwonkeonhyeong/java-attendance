package attendance;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AttendanceRecordsTest {

  private final AttendanceRecords attendanceRecords = new AttendanceRecords();

  @DisplayName("출석_기록_저장_후_저장된_기록_반환")
  @Test
  void saveAttendanceRecordTest() {
    AttendanceRecord attendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0));
    AttendanceRecord savedAttendanceRecord = attendanceRecords.save(attendanceRecord);
    Assertions.assertThat(savedAttendanceRecord).isEqualTo(attendanceRecord);
  }

  @DisplayName("동일한_출석_기록이_존재하는_경우_예외_발생")
  @Test
  void validateExistsAttendanceRecordTest() {
    AttendanceRecord attendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0));
    attendanceRecords.save(attendanceRecord);
    assertThatThrownBy(() -> attendanceRecords.save(attendanceRecord))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 일 출석 기록이 이미 존재합니다");
  }
}
