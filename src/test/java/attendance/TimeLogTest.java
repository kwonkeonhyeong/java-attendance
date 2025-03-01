package attendance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimeLogTest {

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
          new TimeLog(dateTime)
      ).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("캠퍼스 운영 시간이 아닙니다");
    }
  }

  @DisplayName("캠퍼스_운영_시간인_경우_TimeLog_생성")
  @Test
  void operationTimeLogTest() {
    List<LocalDateTime> dateTimes = List.of(
        LocalDateTime.of(2025, 2, 28, 8, 0),
        LocalDateTime.of(2025, 2, 28, 23, 0)
    );

    for (LocalDateTime dateTime : dateTimes) {
      assertThatCode(() ->
          new TimeLog(dateTime)
      ).doesNotThrowAnyException();
    }
  }

  @DisplayName("주말인_경우_TimeLog_생성_시_예외_발생")
  @Test
  void weekendTimeExceptionLogTest() {
    LocalDateTime sunday = LocalDateTime.of(2025, 3, 1, 10, 0);
    LocalDateTime saturday = LocalDateTime.of(2025, 3, 2, 10, 0);

    assertAll(
        () -> assertThatThrownBy(() -> new TimeLog(saturday))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주말 또는 공휴일에는 운영하지 않습니다."),
        () -> assertThatThrownBy(() -> new TimeLog(sunday))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.")
    );
  }

  @DisplayName("공휴일인_경우_TimeLog_생성_시_예외_발생")
  @Test
  void holidayTimeLogExceptionTest() {
    LocalDateTime christmas = LocalDateTime.of(2025, 12, 25, 10, 0);

    assertThatThrownBy(() -> new TimeLog(christmas))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.");
  }

  @DisplayName("평일인_경우_TimeLog_생성")
  @Test
  void weekdayTimeLogTest() {
    LocalDateTime weekday = LocalDateTime.of(2025, 2, 28, 10, 0);
    assertThatCode(() -> new TimeLog(weekday)).doesNotThrowAnyException();
  }

  @DisplayName("TimeLog_동등성_확인")
  @Test
  void timeLogEqualsTest() {
    assertThat(new TimeLog(LocalDateTime.of(2025, 2, 28, 10, 0)).equals(
        new TimeLog(LocalDateTime.of(2025, 2, 28, 10, 0)))).isTrue();
    assertThat(new TimeLog(LocalDateTime.of(2025, 2, 28, 10, 0)).equals(
        new TimeLog(LocalDateTime.of(2025, 2, 27, 10, 0)))).isFalse();
    assertThat(new TimeLog(LocalDateTime.of(2025, 2, 28, 10, 0)).equals(
        new TimeLog(LocalDateTime.of(2025, 3, 28, 10, 0)))).isFalse();
    assertThat(new TimeLog(LocalDateTime.of(2025, 2, 28, 10, 0)).equals(
        new TimeLog(LocalDateTime.of(2025, 2, 28, 10, 5)))).isFalse();
  }

  @DisplayName("TimeLog_내의_기록이_월요일인_경우_true")
  @Test
  void mondayTest() {
    TimeLog timeLog = new TimeLog(LocalDateTime.of(2025, 3, 3, 10, 0));
    assertThat(timeLog.isMonday()).isTrue();
  }

  @DisplayName("TimeLog_내의_기록이_월요일이_아닌_경우_false")
  @Test
  void weekdayTest() {
    TimeLog timeLog = new TimeLog(LocalDateTime.of(2025, 3, 4, 10, 0));
    assertThat(timeLog.isMonday()).isFalse();
  }

  @DisplayName("월요일_TimeLog_내의_기록이_출석_데드라인을_넘지_않은_경우_true")
  @Test
  void mondayAttendanceDeadlineTrueTest() {
    TimeLog mondayMinTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 3, 13, 5));
    assertThat(mondayMinTimeLog.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isTrue();

  }

  @DisplayName("평일_TimeLog_내의_기록이_출석_데드라인을_넘지_않은_경우_true")
  @Test
  void weekdayAttendanceDeadlineTrueTest() {
    TimeLog mondayMinTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 4, 10, 5));
    assertThat(mondayMinTimeLog.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isTrue();

  }

  @DisplayName("월요일_TimeLog_내의_기록이_출석_데드라인을_넘은_경우_false")
  @Test
  void mondayAttendanceDeadlineFalseTest() {
    TimeLog mondayTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 3, 13, 6));
    assertThat(mondayTimeLog.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isFalse();
  }

  @DisplayName("평일_TimeLog_내의_기록이_출석_데드라인을_넘은_경우_false")
  @Test
  void weekdayAttendanceDeadlineFalseTest() {
    TimeLog weekdayTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 4, 10, 6));
    assertThat(weekdayTimeLog.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(ATTENDANCE_APPROVAL_MINUTE))).isFalse();
  }

  @DisplayName("월요일_TimeLog_내의_기록이_지각_데드라인을_넘지_않은_경우_true")
  @Test
  void mondayLateDeadlineTrueTest() {
    TimeLog mondayMinTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 3, 13, 6));
    TimeLog mondayMaxTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 3, 13, 30));
    assertAll(
        () -> assertThat(mondayMinTimeLog.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue(),
        () -> assertThat(mondayMaxTimeLog.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue()
    );
  }

  @DisplayName("평일_TimeLog_내의_기록이_지각을_데드라인을_넘지_않은_경우_true")
  @Test
  void weekdayLateDeadlineTrueTest() {
    TimeLog weekdayMinTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 4, 10, 6));
    TimeLog weekdayMaxTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 4, 10, 30));
    assertAll(
        () -> assertThat(weekdayMinTimeLog.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue(),
        () -> assertThat(weekdayMaxTimeLog.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isTrue()
    );
  }

  @DisplayName("월요일_TimeLog_내의_기록이_지각_데드라인을_넘은_경우_false")
  @Test
  void mondayLateDeadlineFalseTest() {
    TimeLog mondayTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 3, 13, 31));
    assertThat(mondayTimeLog.isAttendance(MONDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isFalse();
  }

  @DisplayName("평일_TimeLog_내의_기록이_지각_데드라인을_넘은_경우_false")
  @Test
  void weekdayLateDeadlineFalseTest() {
    TimeLog weekdayTimeLog = new TimeLog(LocalDateTime.of(2025, 3, 4, 10, 31));
    assertThat(weekdayTimeLog.isAttendance(WEEKDAY_ATTENDANCE_START_TIME.plusMinutes(LATE_APPROVAL_MINUTE))).isFalse();
  }

}
