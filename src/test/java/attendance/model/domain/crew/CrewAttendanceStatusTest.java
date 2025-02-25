package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.*;

import attendance.model.domain.log.TimeLogs;
import attendance.model.repository.AttendanceRepository;
import attendance.model.repository.CrewAttendanceDeserializer;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CrewAttendanceStatusTest {

  private AttendanceRepository attendanceRepository;

  @BeforeEach
  void beforeEach() {
    final CrewAttendanceDeserializer crewAttendanceDeserializer = new CrewAttendanceDeserializer();
    final Path crewAttendanceDataPath = Path.of("src/main/resources/attendances_test.csv");
    attendanceRepository = new AttendanceRepository(crewAttendanceDeserializer,
        crewAttendanceDataPath);
  }

  @ParameterizedTest
  @MethodSource("createCrewAttendanceStatusData")
  void 크루의_현재_출석_상태가_관리를_필요로_하는지_확인(String name, boolean isRequired) {
    Crew crew = attendanceRepository.findCrewByName(name).orElse(null);
    TimeLogs timeLogs = attendanceRepository.findTimeLogsByCrew(crew);
    CrewAttendanceStatus crewAttendanceStatus = CrewAttendanceStatus.of(crew, timeLogs);
    assertThat(crewAttendanceStatus.isRequiredManagement()).isEqualTo(isRequired);
  }

  @ParameterizedTest
  @MethodSource("createPolicyAppliedAbsenceCountData")
  void 지각_3회_시_결석_1회로_환산된_출석_기록_확인(String name, int policyAppliedAbsenceCount) {
    Crew crew = attendanceRepository.findCrewByName(name).orElse(null);
    TimeLogs timeLogs = attendanceRepository.findTimeLogsByCrew(crew);
    CrewAttendanceStatus crewAttendanceStatus = CrewAttendanceStatus.of(crew, timeLogs);
    assertThat(crewAttendanceStatus.getPolicyAppliedAbsenceCount()).isEqualTo(policyAppliedAbsenceCount);
  }

  private static Stream<Arguments> createCrewAttendanceStatusData() {
    return Stream.of(
        Arguments.arguments(
            "빙티", true
        ),
        Arguments.arguments(
            "이든", true
        ),
        Arguments.arguments(
            "빙봉", true
        ),
        Arguments.arguments(
            "쿠키", true
        ),
        Arguments.arguments(
            "짱수", true
        )
    );
  }

  private static Stream<Arguments> createPolicyAppliedAbsenceCountData() {
    return Stream.of(
        Arguments.arguments(
            "빙티", 4
        ),
        Arguments.arguments(
            "이든", 3
        ),
        Arguments.arguments(
            "빙봉", 3
        ),
        Arguments.arguments(
            "쿠키", 3
        ),
        Arguments.arguments(
            "짱수", 2
        )
    );
  }

}
