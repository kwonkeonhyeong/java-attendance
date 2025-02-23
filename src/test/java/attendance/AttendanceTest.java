package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.CrewAttendanceLogResponse;
import attendance.dto.RequiresManagementCrewResponse;
import attendance.dto.UpdateAttendanceResponse;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.comprator.DefaultCrewAttendanceComparator;
import attendance.model.domain.log.TimeLogs;
import attendance.model.repository.AttendanceRepository;
import attendance.model.repository.CrewAttendanceDeserializer;
import attendance.model.service.AttendanceService;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AttendanceTest {

  private AttendanceRepository attendanceRepository;
  private AttendanceService attendanceService;

  @BeforeEach
  void beforeEach() {
    final CrewAttendanceDeserializer crewAttendanceDeserializer = new CrewAttendanceDeserializer();
    final Path crewAttendanceDataPath = Path.of("src/main/resources/attendances_test.csv");
    attendanceRepository = new AttendanceRepository(crewAttendanceDeserializer,
        crewAttendanceDataPath);
    attendanceService = new AttendanceService(attendanceRepository);
  }

  // 닉네임과 등교 시간을 입력하면 출석할 수 있다.
  @Test
  void 닉네임과_등교_시간을_입력하면_출석할_수_있다() {

    Crew crew = new Crew("이든");

    LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 16, 10, 3);

    attendanceService.attendance(crew, localDateTime);

    TimeLogs values = attendanceRepository.findTimeLogsByCrew(crew);

    assertThat(values.isContain(localDateTime)).isTrue();
  }

  // 출석 기록을 확인할 수 있다.
  @Test
  void 출석_기록을_확인할_수_있다() {

    String crewName = "이든";

    CrewAttendanceLogResponse attendanceLog = attendanceService.getAttendanceLog(crewName);

    List<AttendanceLogResponse> searchTimeLogs = attendanceLog.getTimeLogs();

    String searchCrewName = attendanceLog.getCrewName();

    assertThat(crewName).isEqualTo(searchCrewName);
    assertThat(searchTimeLogs).isNotEmpty();
  }

  // 이미 출석한 경우, 다시 출석할 수 없으며 수정 기능을 이용하도록 안내한다.
  @Test
  void 이미_출석한_경우_다시_출석할_수_없으며_수정_기능을_이용하도록_안내한다() {

    Crew crew = new Crew("이든");

    LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 16, 10, 3);

    attendanceService.attendance(crew, localDateTime);

    assertThatIllegalStateException()
        .isThrownBy(() -> attendanceService.attendance(crew, localDateTime))
        .withMessage("금일 출석 기록이 이미 존재합니다.");
  }

  // 출석 확인을 수정하려면 닉네임, 수정하려는 날짜, 등교 시간을 입력하여 기록을 수정할 수 있다.
  @Test
  void 출석을_수정할_수_있다() {

    Crew crew = new Crew("이든");

    LocalDateTime attendanceTime = LocalDateTime.of(2024, 12, 16, 14, 20);

    attendanceService.attendance(crew, attendanceTime);

    LocalDateTime timeToEdit = LocalDateTime.of(2024, 12, 16, 14, 30);

    attendanceService.updateAttendance(crew, timeToEdit);

    TimeLogs values = attendanceRepository.findTimeLogsByCrew(crew);

    assertThat(values.isContain(timeToEdit)).isTrue();
  }

  // 수정 후에는 변경 전과 변경 후의 출석 기록을 확인할 수 있다.
  @Test
  void 수정_후에는_변경_전과_변경_후의_출석_기록을_확인할_수_있다() {

    Crew crew = new Crew("이든");

    LocalDateTime attendanceTime = LocalDateTime.of(2024, 12, 16, 10, 20);

    attendanceService.attendance(crew, attendanceTime);

    LocalDateTime timeToEdit = LocalDateTime.of(2024, 12, 16, 10, 4);

    UpdateAttendanceResponse updateAttendanceResponse = attendanceService.updateAttendance(crew,
        timeToEdit);

    assertThat(updateAttendanceResponse.getBefore()).isEqualTo(attendanceTime);
    assertThat(updateAttendanceResponse.getAfter()).isEqualTo(timeToEdit);
  }

  // 닉네임을 입력하여 크루 출석 기록을 확인할 수 있다.
  @ParameterizedTest
  @MethodSource("getCrewAttendanceLogExpectedValue")
  void 닉네임을_입력하여_크루_출석_기록_확인(String name, int attendanceCount, int lateCount, int absenceCount,
      String managementStatus) {

    CrewAttendanceLogResponse attendanceLog = attendanceService.getAttendanceLog(name);

    assertAll(
        () -> assertThat(attendanceLog.getCrewName()).isEqualTo(name),
        () -> assertThat(attendanceLog.getAttendanceCount()).isEqualTo(attendanceCount),
        () -> assertThat(attendanceLog.getLateCount()).isEqualTo(lateCount),
        () -> assertThat(attendanceLog.getAbsenceCount()).isEqualTo(absenceCount),
        () -> assertThat(attendanceLog.getManagementStatus()).isEqualTo(managementStatus),
        () -> assertThat(
            attendanceLog.getTimeLogs().stream().map(AttendanceLogResponse::getDate)).isSorted()
    );
  }

  static Stream<Arguments> getCrewAttendanceLogExpectedValue() {
    return Stream.of(
        Arguments.arguments(
            "빙티", 3, 4, 3, "면담"
        ),
        Arguments.arguments(
            "이든", 3, 5, 2, "면담"
        ),
        Arguments.arguments(
            "빙봉", 3, 6, 1, "면담"
        ),
        Arguments.arguments(
            "쿠키", 5, 3, 2, "면담"
        ),
        Arguments.arguments(
            "짱수", 8, 0, 2, "경고"
        )
    );
  }

  @ParameterizedTest
  @MethodSource("getManagementCrewExpectedValue")
  void 전날까지의_크루_출석_기록을_통해_제적_위험자_반환(String name, int lateCount, int absenceCount,
      String managementStatus) {

    List<RequiresManagementCrewResponse> requiresManagementCrewResponses = attendanceService.getRequiresManagementCrews(
        new DefaultCrewAttendanceComparator());

    for (RequiresManagementCrewResponse requiresManagementCrewResponse : requiresManagementCrewResponses) {
      if (requiresManagementCrewResponse.getCrewName().equals(name)) {
        assertAll(
            () -> assertThat(requiresManagementCrewResponse.getCrewName()).isEqualTo(name),
            () -> assertThat(requiresManagementCrewResponse.getLateCount()).isEqualTo(lateCount),
            () -> assertThat(requiresManagementCrewResponse.getAbsenceCount()).isEqualTo(
                absenceCount),
            () -> assertThat(requiresManagementCrewResponse.getManagementStatus()).isEqualTo(
                managementStatus)
        );
      }
    }
  }

  static Stream<Arguments> getManagementCrewExpectedValue() {
    return Stream.of(
        Arguments.arguments(
            "빙티", 4, 3, "면담"
        ),
        Arguments.arguments(
            "이든", 5, 2, "면담"
        ),
        Arguments.arguments(
            "빙봉", 6, 1, "면담"
        ),
        Arguments.arguments(
            "쿠키", 3, 2, "면담"
        ),
        Arguments.arguments(
            "짱수", 0, 2, "경고"
        )
    );
  }
}
