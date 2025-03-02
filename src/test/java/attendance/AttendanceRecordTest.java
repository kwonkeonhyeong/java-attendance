package attendance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AttendanceRecordTest {

  private final LocalTime MONDAY_ATTENDANCE_START_TIME = LocalTime.of(13, 0);
  private final LocalTime WEEKDAY_ATTENDANCE_START_TIME = LocalTime.of(10, 0);
  private final long ATTENDANCE_APPROVAL_MINUTE = 5;
  private static final long LATE_APPROVAL_MINUTE = 30;

  @DisplayName("캠퍼스_운영_시간이_아닌_경우_예외_발생")
  @Test
  void operationTimeExceptionTest() {
    List<LocalDateTime> dateTimes = List.of(
        LocalDateTime.of(2025, 2, 28, 7, 59),
        LocalDateTime.of(2025, 2, 28, 23, 1)
    );

    for (LocalDateTime dateTime : dateTimes) {
      assertThatThrownBy(() ->
          new AttendanceRecord(dateTime)
      ).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("캠퍼스 운영 시간이 아닙니다");
    }
  }

  @DisplayName("캠퍼스_운영_시간인_경우_AttendanceRecord_생성")
  @Test
  void operationAttendanceRecordTest() {
    List<LocalDateTime> dateTimes = List.of(
        LocalDateTime.of(2025, 2, 28, 8, 0),
        LocalDateTime.of(2025, 2, 28, 23, 0)
    );

    for (LocalDateTime dateTime : dateTimes) {
      assertThatCode(() ->
          new AttendanceRecord(dateTime)
      ).doesNotThrowAnyException();
    }
  }

  @DisplayName("주말인_경우_AttendanceRecord_생성_시_예외_발생")
  @Test
  void weekendTimeExceptionLogTest() {
    LocalDateTime sunday = LocalDateTime.of(2025, 3, 1, 10, 0);
    LocalDateTime saturday = LocalDateTime.of(2025, 3, 2, 10, 0);

    assertAll(
        () -> assertThatThrownBy(() -> new AttendanceRecord(saturday))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주말 또는 공휴일에는 운영하지 않습니다."),
        () -> assertThatThrownBy(() -> new AttendanceRecord(sunday))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.")
    );
  }

  @DisplayName("공휴일인_경우_AttendanceRecord_생성_시_예외_발생")
  @Test
  void holidayAttendanceRecordExceptionTest() {
    LocalDateTime christmas = LocalDateTime.of(2025, 12, 25, 10, 0);

    assertThatThrownBy(() -> new AttendanceRecord(christmas))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.");
  }

  @DisplayName("평일인_경우_AttendanceRecord_생성")
  @Test
  void weekdayAttendanceRecordTest() {
    LocalDateTime weekday = LocalDateTime.of(2025, 2, 28, 10, 0);
    assertThatCode(() -> new AttendanceRecord(weekday)).doesNotThrowAnyException();
  }

  @DisplayName("날짜가_같은_날은_동일한_AttendanceRecord")
  @Test
  void equalsAttendanceRecordTest() {
    assertThat(new AttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0)).equals(
        new AttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0)))).isTrue();
    assertThat(new AttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0)).equals(
        new AttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 5)))).isTrue();
  }

  @DisplayName("날짜가_다른_날은_동일하지_않은_AttendanceRecord")
  @Test
  void notEqualsAttendanceRecordTest() {
    assertThat(new AttendanceRecord(LocalDateTime.of(2025, 2, 27, 10, 0)).equals(
        new AttendanceRecord(LocalDateTime.of(2025, 2, 28, 10, 0)))).isFalse();
  }

  @DisplayName("AttendanceRecord_내의_기록이_월요일인_경우_true")
  @Test
  void mondayTest() {
    AttendanceRecord attendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 3, 10, 0));
    assertThat(attendanceRecord.isMonday()).isTrue();
  }

  @DisplayName("AttendanceRecord_내의_기록이_월요일이_아닌_경우_false")
  @Test
  void weekdayTest() {
    AttendanceRecord attendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 4, 10, 0));
    assertThat(attendanceRecord.isMonday()).isFalse();
  }

  @DisplayName("월요일_AttendanceRecord_내의_기록이_출석_데드라인을_넘지_않은_경우_true")
  @Test
  void mondayAttendanceDeadlineTrueTest() {
    AttendanceRecord mondayMinAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 5));
    assertThat(mondayMinAttendanceRecord.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isTrue();

  }

  @DisplayName("평일_AttendanceRecord_내의_기록이_출석_데드라인을_넘지_않은_경우_true")
  @Test
  void weekdayAttendanceDeadlineTrueTest() {
    AttendanceRecord mondayMinAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 4, 10, 5));
    assertThat(mondayMinAttendanceRecord.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isTrue();

  }

  @DisplayName("월요일_AttendanceRecord_내의_기록이_출석_데드라인을_넘은_경우_false")
  @Test
  void mondayAttendanceDeadlineFalseTest() {
    AttendanceRecord mondayAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 6));
    assertThat(mondayAttendanceRecord.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isFalse();
  }

  @DisplayName("평일_AttendanceRecord_내의_기록이_출석_데드라인을_넘은_경우_false")
  @Test
  void weekdayAttendanceDeadlineFalseTest() {
    AttendanceRecord weekdayAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 4, 10, 6));
    assertThat(weekdayAttendanceRecord.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isFalse();
  }

  @DisplayName("월요일_AttendanceRecord_내의_기록이_지각_데드라인을_넘지_않은_경우_true")
  @Test
  void mondayLateDeadlineTrueTest() {
    AttendanceRecord mondayMinAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 6));
    AttendanceRecord mondayMaxAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 30));
    assertAll(
        () -> assertThat(mondayMinAttendanceRecord.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue(),
        () -> assertThat(mondayMaxAttendanceRecord.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue()
    );
  }

  @DisplayName("평일_AttendanceRecord_내의_기록이_지각을_데드라인을_넘지_않은_경우_true")
  @Test
  void weekdayLateDeadlineTrueTest() {
    AttendanceRecord weekdayMinAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 4, 10, 6));
    AttendanceRecord weekdayMaxAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 4, 10, 30));
    assertAll(
        () -> assertThat(weekdayMinAttendanceRecord.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue(),
        () -> assertThat(weekdayMaxAttendanceRecord.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue()
    );
  }

  @DisplayName("월요일_AttendanceRecord_내의_기록이_지각_데드라인을_넘은_경우_false")
  @Test
  void mondayLateDeadlineFalseTest() {
    AttendanceRecord mondayAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 3, 13, 31));
    assertThat(mondayAttendanceRecord.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isFalse();
  }

  @DisplayName("평일_AttendanceRecord_내의_기록이_지각_데드라인을_넘은_경우_false")
  @Test
  void weekdayLateDeadlineFalseTest() {
    AttendanceRecord weekdayAttendanceRecord = new AttendanceRecord(LocalDateTime.of(2025, 3, 4, 10, 31));
    assertThat(weekdayAttendanceRecord.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isFalse();
  }

  @DisplayName("저장된_시간을_변경하고_새로운_AttendanceRecord_반환")
  @Test
  void modifyAttendance() {
    AttendanceRecord attendanceRecord = new AttendanceRecord(LocalDateTime.of(2025,2,28,10,0));
    AttendanceRecord modifiedAttendanceRecord = attendanceRecord.modifyTime(LocalTime.of(10,5));
    assertThat(attendanceRecord).isNotSameAs(modifiedAttendanceRecord);
    assertThat(modifiedAttendanceRecord.getDateTime().toLocalTime()).isEqualTo(LocalTime.of(10,5));
  }


}
