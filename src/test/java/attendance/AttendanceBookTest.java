package attendance;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AttendanceBookTest {

  private final AttendanceBook attendanceBook = new AttendanceBook(
      new HashMap<>(
          Map.of(
              new Crew("히포"), new AttendanceRecords()
          )
      )
  );

  @DisplayName("출석_확인_시_크루가_존재하지_않는_경우_예외_발생")
  @Test
  void notExistsCrewTest() {
    Assertions.assertThatThrownBy(() ->
      attendanceBook.checkAttendance("없는크루", LocalDateTime.of(2025,2,28,10,0))
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록되지 않은 크루입니다");
  }

  @DisplayName("출석_확인_시_저장된_출석_기록_반환")
  @Test
  void checkAttendanceTest() {
    LocalDateTime dateTime = LocalDateTime.of(2025, 2, 28, 10, 0);
    AttendanceRecord attendanceRecord = attendanceBook.checkAttendance("히포",
        LocalDateTime.of(2025, 2, 28, 10, 0));

    assertThat(attendanceRecord.getDateTime()).isEqualTo(dateTime);
  }

  @DisplayName("출석_확인_시_해당_출석_상태_확인")
  @ParameterizedTest
  @MethodSource("createDateTimeAndAttendanceStatus")
  void checkAttendanceStatus(LocalDateTime dateTime, AttendanceStatus status) {
    AttendanceRecord attendanceRecord = attendanceBook.checkAttendance("히포",
        dateTime);
    assertThat(AttendanceStatus.from(attendanceRecord)).isEqualTo(status);
  }

  private static Stream<Arguments> createDateTimeAndAttendanceStatus() {
    return Stream.of(
        Arguments.arguments(
            LocalDateTime.of(2025, 2, 26, 10, 0),
            AttendanceStatus.ATTENDANCE
        ),
        Arguments.arguments(
            LocalDateTime.of(2025, 2, 27, 10, 6),
            AttendanceStatus.LATE
        ),
        Arguments.arguments(
            LocalDateTime.of(2025, 2, 28, 10, 31),
            AttendanceStatus.ABSENCE
        )
    );
  }
}
