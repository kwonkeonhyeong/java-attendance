package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AttendanceStatusTest {

  @DisplayName("평일_TimeLog_확인하여_시간에_맞는_AttendanceStatus_반환")
  @Test
  void judgeAttendanceStatusByWeekdayTimeLogTest() {
    assertAll(
        () -> assertThat(
            AttendanceStatus.from(
                new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 5)))).isEqualTo(
            AttendanceStatus.ATTENDANCE),
        () -> assertThat(
            AttendanceStatus.from(
                new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 6)))).isEqualTo(
            AttendanceStatus.LATE),
        () -> assertThat(
            AttendanceStatus.from(
                new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 30)))).isEqualTo(
            AttendanceStatus.LATE),
        () -> assertThat(
            AttendanceStatus.from(
                new ExistentAttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 31)))).isEqualTo(
            AttendanceStatus.ABSENCE)
    );
  }

  @DisplayName("월요일_TimeLog_확인하여_시간에_맞는_AttendanceStatus_반환")
  @Test
  void judgeAttendanceStatusByMondayTimeLogTest() {
    assertAll(
        () -> assertThat(AttendanceStatus.from(
            new ExistentAttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 5)))).isEqualTo(
            AttendanceStatus.ATTENDANCE),
        () -> assertThat(AttendanceStatus.from(
            new ExistentAttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 6)))).isEqualTo(
            AttendanceStatus.LATE),
        () -> assertThat(AttendanceStatus.from(
            new ExistentAttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 30)))).isEqualTo(
            AttendanceStatus.LATE),
        () -> assertThat(AttendanceStatus.from(
            new ExistentAttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 31)))).isEqualTo(
            AttendanceStatus.ABSENCE)
    );
  }

  @DisplayName("출석이_없는_출석_기록의_경우_결석으로_처리")
  @Test
  void judgeAbsenceTest() {
    assertThat(AttendanceStatus.from(new AbsenceRecord(LocalDate.of(2025, 2, 28)))).isEqualTo(AttendanceStatus.ABSENCE);
  }
}
