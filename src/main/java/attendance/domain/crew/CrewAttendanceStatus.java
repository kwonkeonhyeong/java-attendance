package attendance.domain.crew;

import java.util.Map;

public class CrewAttendanceStatus {

  private final Crew crew;
  private final Map<AttendanceStatus, Long> attendanceStatusCounts;

  private CrewAttendanceStatus(Crew crew, Map<AttendanceStatus, Long> attendanceStatusCounts) {
    this.crew = crew;
    this.attendanceStatusCounts = attendanceStatusCounts;
  }

  public static CrewAttendanceStatus of(Crew crew, TimeLogs timeLogs) {
    Map<AttendanceStatus, Long> attendanceStatusCounts = Map.of(
        AttendanceStatus.ATTENDANCE, timeLogs.calculateAttendanceCount(),
        AttendanceStatus.LATE, timeLogs.calculateLateCount(),
        AttendanceStatus.ABSENCE, timeLogs.calculateAbsenceCount()
    );

    return new CrewAttendanceStatus(
        crew,
        attendanceStatusCounts
    );
  }

  public long calculatePolicyAppliedAbsenceCount() {
    long absenceCount = getAbsenceCount();
    return absenceCount + applyPolicyToLateCount();
  }

  public long applyPolicyToLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE) / 3;
  }

  public long calculateRemainingLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE) % 3;
  }

  public boolean isRequiredManagement() {
    return !getManagementStatus().equals(ManagementStatus.NONE);
  }

  public long getAbsenceCount() {
    return attendanceStatusCounts.get(AttendanceStatus.ABSENCE);
  }

  public long getLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE);
  }

  public ManagementStatus getManagementStatus() {
    return ManagementStatus.of(calculatePolicyAppliedAbsenceCount());
  }

  public Crew getCrew() {
    return crew;
  }

}
