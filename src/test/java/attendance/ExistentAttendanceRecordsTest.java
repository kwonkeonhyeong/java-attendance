package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExistentAttendanceRecordsTest {

  private final AttendanceRecords attendanceRecords = new AttendanceRecords();

  @DisplayName("출석_기록_저장_후_저장된_기록_반환")
  @Test
  void saveAttendanceRecordTest() {
    ExistentAttendanceRecord existentAttendanceRecord = new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0));
    ExistentAttendanceRecord savedExistentAttendanceRecord = attendanceRecords.save(
        existentAttendanceRecord);
    assertThat(savedExistentAttendanceRecord).isEqualTo(existentAttendanceRecord);
  }

  @DisplayName("동일한_출석_기록이_존재하는_경우_예외_발생")
  @Test
  void validateExistsAttendanceRecordTest() {
    ExistentAttendanceRecord existentAttendanceRecord = new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0));
    attendanceRecords.save(existentAttendanceRecord);
    assertThatThrownBy(() -> attendanceRecords.save(existentAttendanceRecord))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 일 출석 기록이 이미 존재합니다");
  }

  @DisplayName("저장된_AttendanceRecord를_수정하고_수정_이전의_기록_반환")
  @Test
  void modifyAttendanceRecordAndReturnPreviousRecordTest() {
    ExistentAttendanceRecord existentAttendanceRecord = new ExistentAttendanceRecord(LocalDateTime.of(2025,2,28,10,0));
    attendanceRecords.save(existentAttendanceRecord);
    ExistentAttendanceRecord updateExistentAttendanceRecord = new ExistentAttendanceRecord(LocalDateTime.of(2025,2,28,10,0));
    AttendanceRecord previousRecord= attendanceRecords.modifyAttendanceRecord(
        updateExistentAttendanceRecord);
    assertThat(existentAttendanceRecord).isEqualTo(previousRecord);
  }

  @DisplayName("존재하지_않은_출석_기록을_수정하고_수정_이전의_기록_반환")
  @Test
  void notExistsAttendanceRecordModificationTest() {
    ExistentAttendanceRecord updateExistentAttendanceRecord = new ExistentAttendanceRecord(LocalDateTime.of(2025,2,28,10,0));
    AttendanceRecord previousRecord= attendanceRecords.modifyAttendanceRecord(
        updateExistentAttendanceRecord);
    assertThat(previousRecord).isInstanceOf(AbsenceRecord.class);
  }

}
