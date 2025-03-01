package attendance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AttendanceRecordTest {

  @DisplayName("크루_닉네임과_출석_시간을_통해_AttendanceRecord_내에서_Crew_TimeLog_생성")
  @Test
  void attendanceRecordConstructorTest() {
    AttendanceRecord attendanceRecord = new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 0));

    assertAll(
        () -> assertThat(attendanceRecord.getNickname()).isInstanceOf(Crew.class),
        () -> assertThat(attendanceRecord.getTimeLog()).isInstanceOf(TimeLog.class)
    );
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
