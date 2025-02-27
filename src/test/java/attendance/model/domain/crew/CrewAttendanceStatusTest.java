package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import attendance.domain.crew.Crew;
import attendance.domain.crew.CrewAttendanceStatus;
import attendance.domain.crew.TimeLog;
import attendance.domain.crew.TimeLogs;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CrewAttendanceStatusTest {


  private final CrewAttendanceStatus managementCrewStatus = CrewAttendanceStatus.of(new Crew("이든"), createRequiredManagementCrewTimeLogs());
  private final CrewAttendanceStatus crewStatus = CrewAttendanceStatus.of(new Crew("히포"), createCrewTimeLogs());

  @Test
  void 크루의_현재_출석_상태가_관리를_필요로_하는_경우_TRUE() {
    assertThat(managementCrewStatus.isRequiredManagement()).isTrue();
  }

  @Test
  void 크루의_현재_출석_상태가_관리를_필요로_하는_경우_FALSE() {
    assertThat(crewStatus.isRequiredManagement()).isFalse();
  }

  @Test
  void 지각_3회_시_결석_1회로_환산된_출석_기록_확인() {
    assertAll(
        () ->assertThat(managementCrewStatus.calculatePolicyAppliedAbsenceCount()).isEqualTo(3),
        () ->assertThat(crewStatus.calculatePolicyAppliedAbsenceCount()).isEqualTo(0)
    );
  }

  @Test
  void 지각_3회_시_결석_1회로_반환_후_남은_지각_횟수_반환() {
    assertAll(
        () -> assertThat(managementCrewStatus.calculateRemainingLateCount()).isEqualTo(1),
        () -> assertThat(crewStatus.calculateRemainingLateCount()).isEqualTo(0)
    );
  }

  private TimeLogs createRequiredManagementCrewTimeLogs() {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 6)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 4, 10, 3)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 5, 10, 31)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 6, 10, 30)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 9, 13, 5)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 10, 10, 17)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 11, 10, 3)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 12, 10, 34)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 13, 10, 7)));
    return new TimeLogs(logs);
  }

  private TimeLogs createCrewTimeLogs() {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 2)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 4, 10, 3)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 5, 10, 4)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 6, 10, 5)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 9, 13, 5)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 10, 10, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 11, 10, 2)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 12, 10, 3)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 13, 10, 4)));
    return new TimeLogs(logs);
  }

}
