package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;

import attendance.domain.crew.AttendanceStatusCounts;
import attendance.domain.crew.Crew;
import attendance.repository.AttendanceRepository;
import attendance.repository.CrewTimeLogsInitializer;
import attendance.repository.DefaultCrewTimeLogsInitializer;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AttendanceStatusCountsTest {

  private AttendanceRepository attendanceRepository;
  private AttendanceStatusCounts attendanceStatusCounts;

  @BeforeEach
  void beforeEach() {
    final CrewTimeLogsInitializer crewTimeLogsInitializer = new DefaultCrewTimeLogsInitializer();
    final Path crewAttendanceDataPath = Path.of("src/main/resources/attendances_test.csv");
    attendanceRepository = new AttendanceRepository(crewTimeLogsInitializer,
        crewAttendanceDataPath);
    attendanceStatusCounts = AttendanceStatusCounts.of(attendanceRepository.findTimeLogsByCrew(new Crew("이든")));
  }

  @Test
  void 지각_3회_시_결석_1회로_변환한_계산_값_반환() {
    assertThat(attendanceStatusCounts.calculatePolicyAppliedAbsenceCount()).isEqualTo(3);
  }

  @Test
  void 지각_3회_시_결석_1회로_반환_후_남은_지각_횟수_반환() {
    assertThat(attendanceStatusCounts.calculateRemainingLateCount()).isEqualTo(2);
  }

}
