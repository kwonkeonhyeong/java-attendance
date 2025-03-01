package attendance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TimeLogTest {

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

  @DisplayName("AttendanceRecord_동등성_확인")
  @ParameterizedTest
  @MethodSource("createAttendanceRecord")
  void attendanceRecordEqualsTest(AttendanceRecord record, AttendanceRecord anotherRecord, boolean expected) {
    assertThat(record.equals(anotherRecord)).isEqualTo(expected);
  }

  static Stream<Arguments> createAttendanceRecord() {
    return Stream.of(
        Arguments.arguments(
            new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 0)),
            new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 0)),
            true
        ),
        Arguments.arguments(
            new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 0)),
            new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 5)),
            false
        ),
        Arguments.arguments(
            new AttendanceRecord("히포", LocalDateTime.of(2025, 3, 3, 10, 0)),
            new AttendanceRecord("이든", LocalDateTime.of(2025, 3, 4, 10, 0)),
            false
        ),
        Arguments.arguments(
            new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 0)),
            new AttendanceRecord("이든", LocalDateTime.of(2025, 2, 28, 10, 0)),
            false
        )
    );
  }

}
