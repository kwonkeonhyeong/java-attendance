package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import attendance.domain.crew.AttendanceStatus;
import attendance.domain.crew.TimeLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class TimeLogTest {

  @Test
  void TimeLog와_날짜가_같으면_true를_반환() {
    TimeLog log = TimeLog.from(LocalDateTime.of(2024, 12, 13, 13, 0));

    assertThat(log.isSame(LocalDate.of(2024, 12, 13))).isTrue();
  }

  @Test
  void TimeLog와_날짜가_같으면_false를_반환() {
    TimeLog log = TimeLog.from(LocalDateTime.of(2024, 12, 13, 13, 0));

    assertThat(log.isSame(LocalDate.of(2024, 12, 13))).isTrue();
  }

  @ParameterizedTest
  @MethodSource("createExpectedAttendanceStatusForMonday")
  void 월요일_출석_시간에_따라_출석_상태를_반환(TimeLog timeLog, AttendanceStatus attendanceStatus) {
    assertThat(timeLog.judgeAttendanceStatus()).isEqualTo(attendanceStatus);
  }

  private static Stream<Arguments> createExpectedAttendanceStatusForMonday() {
    return Stream.of(
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 0)),
            AttendanceStatus.ATTENDANCE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 1)),
            AttendanceStatus.ATTENDANCE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 5)),
            AttendanceStatus.ATTENDANCE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 6)),
            AttendanceStatus.LATE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 30)),
            AttendanceStatus.LATE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 31)),
            AttendanceStatus.ABSENCE
        )
    );
  }

  @ParameterizedTest
  @MethodSource("createExpectedAttendanceStatusForWeekDay")
  void 평일_출석_시간에_따른_출석_상태를_반환(TimeLog timeLog, AttendanceStatus attendanceStatus) {
    assertThat(timeLog.judgeAttendanceStatus()).isEqualTo(attendanceStatus);
  }

  private static Stream<Arguments> createExpectedAttendanceStatusForWeekDay() {
    return Stream.of(
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 0)),
            AttendanceStatus.ATTENDANCE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 1)),
            AttendanceStatus.ATTENDANCE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 5)),
            AttendanceStatus.ATTENDANCE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 6)),
            AttendanceStatus.LATE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 30)),
            AttendanceStatus.LATE
        ),
        Arguments.arguments(
            TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 31)),
            AttendanceStatus.ABSENCE
        )
    );
  }

  @ParameterizedTest
  @MethodSource("createUnavailableDateTime")
  void 출석_시간이_08_00시_부터_23_00시_사이가_아닌_경우_예외_발생(LocalDateTime unavailableDateTime) {
    assertThatThrownBy(() -> TimeLog.from(unavailableDateTime))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("캠퍼스 운영시간이 아닙니다 (운영시간 매일 08:00~23:00)");
  }


  @ParameterizedTest
  @MethodSource("createAvailableDateTime")
  void 출석_시간이_08_00시_부터_23_00시_사이인_아닌_경우_TimeLog_생성(LocalDateTime availableDateTime) {
    assertThatCode(() -> TimeLog.from(availableDateTime))
        .doesNotThrowAnyException();
  }

  private static Stream<Arguments> createUnavailableDateTime() {
    return Stream.of(
        Arguments.arguments(
            LocalDateTime.of(2024, 12, 3, 7, 59)
        ),
        Arguments.arguments(
            LocalDateTime.of(2024, 12, 3, 23, 1)
        )
    );
  }

  private static Stream<Arguments> createAvailableDateTime() {
    return Stream.of(
        Arguments.arguments(
            LocalDateTime.of(2024, 12, 3, 8, 0)
        ),
        Arguments.arguments(
            LocalDateTime.of(2024, 12, 3, 23, 0)
        )

    );
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 7, 8, 14, 15, 21, 22, 28, 29})
  void 주말에_출석_기록을_생성하는_경우_예외_발생(int date) {
    LocalDate weekEnd = LocalDate.of(2024, 12, date);
    assertThatThrownBy(() -> TimeLog.of(weekEnd, LocalTime.of(10, 30)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주말 또는 공휴일에는 출석 기록을 생성할 수 없습니다");

  }

  @Test
  void 공휴일에_출석_기록을_생성하는_경우_예외_발생() {
    LocalDate christmas = LocalDate.of(2024, 12, 25);
    assertThatThrownBy(() -> TimeLog.of(christmas, LocalTime.of(10, 30)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주말 또는 공휴일에는 출석 기록을 생성할 수 없습니다");
  }

}
