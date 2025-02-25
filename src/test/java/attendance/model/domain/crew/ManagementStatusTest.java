package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ManagementStatusTest {

  @Test
  void 출석_및_지각_count_를_기준으로_크루_관리_상태를_확인() {
    CrewAttendanceStatus expulsionTimeLogs = createExpulsionCrewAttendanceStatus();
    CrewAttendanceStatus counselingTimeLogs = createCounselingCrewAttendanceStatus();
    CrewAttendanceStatus warningTimeLogs = createWarningCrewAttendanceStatus();

    ManagementStatus expulsionManagementStatus = ManagementStatus.of(expulsionTimeLogs.getPolicyAppliedAbsenceCount());
    ManagementStatus counselingManagementStatus = ManagementStatus.of(counselingTimeLogs.getPolicyAppliedAbsenceCount());
    ManagementStatus warningManagementStatus = ManagementStatus.of(warningTimeLogs.getPolicyAppliedAbsenceCount());

    assertThat(expulsionManagementStatus).isEqualTo(ManagementStatus.EXPULSION);
    assertThat(counselingManagementStatus).isEqualTo(ManagementStatus.COUNSELING);
    assertThat(warningManagementStatus).isEqualTo(ManagementStatus.WARNING);
  }

  private CrewAttendanceStatus createExpulsionCrewAttendanceStatus() {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 5)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 4, 10, 5)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 5, 10, 5)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 6, 10, 36)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 9, 13, 32)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 10, 10, 32)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 11, 10, 34)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 12, 10, 34)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 13, 10, 34)));
    return CrewAttendanceStatus.of(new Crew("히포"), new TimeLogs(logs));
  }

  private CrewAttendanceStatus createCounselingCrewAttendanceStatus () {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 4, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 5, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 6, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 9, 13, 33)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 10, 10, 34)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 11, 10, 34)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 12, 10, 34)));
    return CrewAttendanceStatus.of(new Crew("히포"), new TimeLogs(logs));
  }

  private CrewAttendanceStatus createWarningCrewAttendanceStatus () {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 4, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 5, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 6, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 9, 13, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 10, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 11, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 12, 10, 34)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 13, 10, 34)));
    return CrewAttendanceStatus.of(new Crew("히포"), new TimeLogs(logs));
  }

}
