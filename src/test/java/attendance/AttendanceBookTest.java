package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
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
              new Crew("히포"), new AttendanceRecords(),
              new Crew("이든"), new AttendanceRecords(),
              new Crew("짱수"), new AttendanceRecords(),
              new Crew("짱구"), new AttendanceRecords()
          )
      )
  );

  @DisplayName("출석_확인_시_크루가_존재하지_않는_경우_예외_발생")
  @Test
  void notExistsCrewTest() {
    Assertions.assertThatThrownBy(() ->
            attendanceBook.check("없는크루", LocalDateTime.of(2025, 2, 28, 10, 0))
        ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록되지 않은 크루입니다");
  }

  @DisplayName("출석_확인_시_저장된_출석_기록_반환")
  @Test
  void checkTest() {
    LocalDateTime dateTime = LocalDateTime.of(2025, 2, 28, 10, 0);
    ExistentAttendanceRecord existentAttendanceRecord = attendanceBook.check("히포",
        LocalDateTime.of(2025, 2, 28, 10, 0));

    assertThat(existentAttendanceRecord.getDateTime()).isEqualTo(dateTime);
  }

  @DisplayName("출석_확인_시_해당_출석_상태_확인")
  @ParameterizedTest
  @MethodSource("createDateTimeAndAttendanceStatus")
  void checkStatus(LocalDateTime dateTime, AttendanceStatus status) {
    ExistentAttendanceRecord existentAttendanceRecord = attendanceBook.check("히포",
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
        () -> attendanceBook.modify("없는크루", LocalDateTime.of(2025, 2, 28, 10, 0))
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록되지 않은 크루입니다");
  }

  @DisplayName("출석_수정_시_수정_전_기록과_수정_후_기록_반환")
  @Test
  void modifyRecordReturnTest() {
    ExistentAttendanceRecord existentAttendanceRecord = attendanceBook.check("히포",
        LocalDateTime.of(2025, 2, 28, 10, 12));

    LocalDateTime updateDateTime = LocalDateTime.of(2025, 2, 28, 10, 2);
    Entry<AttendanceRecord, AttendanceRecord> updatedAttendanceRecords = attendanceBook.modify(
        "히포", updateDateTime);

    assertThat(updatedAttendanceRecords.getKey().getRecord()).isEqualTo(
        existentAttendanceRecord.getRecord());
    assertThat(updatedAttendanceRecords.getValue().getRecord()).isEqualTo(updateDateTime);
  }

  @DisplayName("출석_기록이_없는_경우에_대한_수정_전_기록과_수정_후_기록_반환")
  @Test
  void modifyAbsenceRecordReturnTest() {
    LocalDateTime updateDateTime = LocalDateTime.of(2025, 2, 28, 10, 2);
    Entry<AttendanceRecord, AttendanceRecord> updatedAttendanceRecords = attendanceBook.modify(
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
        () -> attendanceBook.modify("히포", updateDateTime)
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.");
  }

  @DisplayName("출석_불가능한_시간_수정_시도_시_예외_발생")
  @Test
  void modifyRecordToUnavailableTimeTest() {
    LocalDateTime updateDateTime = LocalDateTime.of(2025, 2, 28, 7, 2);
    assertThatThrownBy(
        () -> attendanceBook.modify("히포", updateDateTime)
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("캠퍼스 운영 시간이 아닙니다");
  }

  @DisplayName("등록되지_않은_크루_출석_조회_시_예외_발생")
  @Test
  void notExistsCrewAttendanceRecordsSearchingTest() {
    assertThatThrownBy(() ->
        attendanceBook.search("없는크루", LocalDate.of(2025, 2, 11))
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록되지 않은 크루입니다");
  }

  @DisplayName("출석_기록과_상태를_정렬하여_조회")
  @Test
  void searchedRecordTest() {
    setRecords();

    LinkedHashMap<LocalDateTime, AttendanceStatus> searched = attendanceBook.search("히포",
        LocalDate.of(2025, 2, 11));

    assertThat(searched).containsAllEntriesOf(createExpectedAttendanceRecord());
  }

  private void setRecords() {
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 3, 13, 6));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 4, 10, 5));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 5, 10, 31));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 6, 10, 13));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 7, 10, 5));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 10, 13, 5));
  }

  private LinkedHashMap<LocalDateTime, AttendanceStatus> createExpectedAttendanceRecord() {
    LinkedHashMap<LocalDateTime, AttendanceStatus> expected = new LinkedHashMap<>();
    expected.put(LocalDateTime.of(2025, 2, 3, 13, 6), AttendanceStatus.LATE);
    expected.put(LocalDateTime.of(2025, 2, 4, 10, 5), AttendanceStatus.ATTENDANCE);
    expected.put(LocalDateTime.of(2025, 2, 5, 10, 31), AttendanceStatus.ABSENCE);
    expected.put(LocalDateTime.of(2025, 2, 6, 10, 13), AttendanceStatus.LATE);
    expected.put(LocalDateTime.of(2025, 2, 7, 10, 5), AttendanceStatus.ATTENDANCE);
    expected.put(LocalDateTime.of(2025, 2, 10, 13, 5), AttendanceStatus.ATTENDANCE);
    return expected;
  }

  @DisplayName("크루의_전날까지_출석_결과를_계산")
  @Test
  void calculateAttendanceResultTest() {
    setRecords();
    Map<AttendanceStatus, Integer> attendanceResult = attendanceBook.calculateAttendanceResult("히포",
        LocalDate.of(2025, 2, 11));
    assertThat(attendanceResult).isEqualTo(createExpectedAttendanceResult());
  }

  private Map<AttendanceStatus, Integer> createExpectedAttendanceResult() {
    Map<AttendanceStatus, Integer> expected = new HashMap<>();
    expected.put(AttendanceStatus.ATTENDANCE, 3);
    expected.put(AttendanceStatus.LATE, 2);
    expected.put(AttendanceStatus.ABSENCE, 1);
    return expected;
  }

  @DisplayName("크루별_제적_위험_상태를_확인")
  @Test
  void checkManagementCrewsTest() {
    setCrewsRecords();
    Map<Crew, ManagementStatus> crewManagementStatusMap = attendanceBook.checkManagementCrews(
        LocalDate.of(2025, 2, 11));
    assertThat(crewManagementStatusMap).isEqualTo(createExpectedManagementStatus());
  }

  private Map<Crew, ManagementStatus> createExpectedManagementStatus() {
    Map<Crew, ManagementStatus> expected = new HashMap<>();
    expected.put(new Crew("히포"), ManagementStatus.WARNING);
    expected.put(new Crew("이든"), ManagementStatus.EXPULSION);
    expected.put(new Crew("짱수"), ManagementStatus.INTERVIEW);
    expected.put(new Crew("짱구"), ManagementStatus.GENERAL);
    return expected;
  }

  private void setCrewsRecords() {
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 3, 13, 6));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 4, 10, 7));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 5, 10, 8));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 6, 10, 9));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 7, 10, 10));
    attendanceBook.check("히포", LocalDateTime.of(2025, 2, 10, 13, 11));

    attendanceBook.check("이든", LocalDateTime.of(2025, 2, 3, 13, 31));
    attendanceBook.check("이든", LocalDateTime.of(2025, 2, 4, 10, 35));
    attendanceBook.check("이든", LocalDateTime.of(2025, 2, 5, 10, 31));
    attendanceBook.check("이든", LocalDateTime.of(2025, 2, 6, 10, 38));

    attendanceBook.check("짱수", LocalDateTime.of(2025, 2, 5, 10, 15));
    attendanceBook.check("짱수", LocalDateTime.of(2025, 2, 6, 10, 13));
    attendanceBook.check("짱수", LocalDateTime.of(2025, 2, 7, 10, 7));
    attendanceBook.check("짱수", LocalDateTime.of(2025, 2, 10, 13, 5));

    attendanceBook.check("짱구", LocalDateTime.of(2025, 2, 3, 13, 1));
    attendanceBook.check("짱구", LocalDateTime.of(2025, 2, 4, 10, 2));
    attendanceBook.check("짱구", LocalDateTime.of(2025, 2, 5, 10, 3));
    attendanceBook.check("짱구", LocalDateTime.of(2025, 2, 6, 10, 4));
    attendanceBook.check("짱구", LocalDateTime.of(2025, 2, 7, 10, 5));
    attendanceBook.check("짱구", LocalDateTime.of(2025, 2, 10, 13, 1));
  }
}
