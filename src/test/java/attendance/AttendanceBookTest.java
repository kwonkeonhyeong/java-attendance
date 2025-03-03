package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.bytebuddy.build.ToStringPlugin.Enhance;
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
            attendanceBook.checkAttendance("없는크루", LocalDateTime.of(2025, 2, 28, 10, 0))
        ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록되지 않은 크루입니다");
  }

  @DisplayName("출석_확인_시_저장된_출석_기록_반환")
  @Test
  void checkAttendanceTest() {
    LocalDateTime dateTime = LocalDateTime.of(2025, 2, 28, 10, 0);
    ExistentAttendanceRecord existentAttendanceRecord = attendanceBook.checkAttendance("히포",
        LocalDateTime.of(2025, 2, 28, 10, 0));

    assertThat(existentAttendanceRecord.getDateTime()).isEqualTo(dateTime);
  }

  @DisplayName("출석_확인_시_해당_출석_상태_확인")
  @ParameterizedTest
  @MethodSource("createDateTimeAndAttendanceStatus")
  void checkAttendanceStatus(LocalDateTime dateTime, AttendanceStatus status) {
    ExistentAttendanceRecord existentAttendanceRecord = attendanceBook.checkAttendance("히포",
        dateTime);
    assertThat(AttendanceStatus.from(existentAttendanceRecord)).isEqualTo(status);
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

  @DisplayName("출석_수정_시_등록되지_않은_크루_예외")
  @Test
  void validateExistentCrewTest() {
    assertThatThrownBy(
        () -> attendanceBook.modifyAttendanceRecord("없는크루", LocalDateTime.of(2025, 2, 28, 10, 0))
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록되지 않은 크루입니다");
  }

  @DisplayName("출석_수정_시_수정_전_기록과_수정_후_기록_반환")
  @Test
  void modifyRecordReturnTest() {
    ExistentAttendanceRecord existentAttendanceRecord = attendanceBook.checkAttendance("히포",
        LocalDateTime.of(2025, 2, 28, 10, 12));

    LocalDateTime updateDateTime = LocalDateTime.of(2025, 2, 28, 10, 2);
    Entry<AttendanceRecord, AttendanceRecord> updatedAttendanceRecords = attendanceBook.modifyAttendanceRecord(
        "히포", updateDateTime);

    assertThat(updatedAttendanceRecords.getKey().getRecord()).isEqualTo(
        existentAttendanceRecord.getRecord());
    assertThat(updatedAttendanceRecords.getValue().getRecord()).isEqualTo(updateDateTime);
  }

  @DisplayName("출석_기록이_없는_경우에_대한_수정_전_기록과_수정_후_기록_반환")
  @Test
  void modifyAbsenceRecordReturnTest() {
    LocalDateTime updateDateTime = LocalDateTime.of(2025, 2, 28, 10, 2);
    Entry<AttendanceRecord, AttendanceRecord> updatedAttendanceRecords = attendanceBook.modifyAttendanceRecord(
        "히포", updateDateTime);

    assertThat(updatedAttendanceRecords.getKey().getRecord()).isEqualTo(
        LocalDateTime.of(2025, 2, 28, 0, 0));
    assertThat(updatedAttendanceRecords.getValue().getRecord()).isEqualTo(updateDateTime);
  }

  @DisplayName("출석_불가능한_날_수정_시도_시_예외_발생")
  @Test
  void modifyRecordToUnavailableDateTest() {
    LocalDateTime updateDateTime = LocalDateTime.of(2025, 3, 1, 10, 2);
    assertThatThrownBy(
        () -> attendanceBook.modifyAttendanceRecord("히포", updateDateTime)
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.");
  }

  @DisplayName("출석_불가능한_시간_수정_시도_시_예외_발생")
  @Test
  void modifyRecordToUnavailableTimeTest() {
    LocalDateTime updateDateTime = LocalDateTime.of(2025, 2, 28, 7, 2);
    assertThatThrownBy(
        () -> attendanceBook.modifyAttendanceRecord("히포", updateDateTime)
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("캠퍼스 운영 시간이 아닙니다");
  }

  @DisplayName("등록되지_않은_크루_출석_조회_시_예외_발생")
  @Test
  void notExistsCrewAttendanceRecordsSearchingTest() {
    LinkedHashMap<LocalDateTime, AttendanceStatus> attendanceRecords = attendanceBook.search("히포");

  }

}
