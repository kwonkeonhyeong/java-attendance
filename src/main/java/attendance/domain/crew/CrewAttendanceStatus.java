package attendance.domain.crew;

import java.util.HashMap;
import java.util.Map;

public class CrewAttendanceStatus {

  private final Crew crew;
  private final Map<AttendanceStatus, Integer> attendanceStatusCounts;

  private CrewAttendanceStatus(Crew crew ,Map<AttendanceStatus, Integer> attendanceStatusCounts) {
    this.crew = crew;
    this.attendanceStatusCounts = attendanceStatusCounts;
  }

  public static CrewAttendanceStatus of(Crew crew, TimeLogs timeLogs) {
    Map<AttendanceStatus, Integer> attendanceStatusCounts = Map.of(
        AttendanceStatus.ATTENDANCE, timeLogs.calculateAttendanceCount(),
        AttendanceStatus.LATE, timeLogs.calculateLateCount(),
        AttendanceStatus.ABSENCE, timeLogs.calculateAbsenceCount()
    );

    return new CrewAttendanceStatus(
        crew,
        attendanceStatusCounts
    );
  }

  public int calculatePolicyAppliedAbsenceCount() {
    int absenceCount = getAbsenceCount();
    return absenceCount + applyPolicyToLateCount();
  }

  public int applyPolicyToLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE) / 3;
  }

  public int calculateRemainingLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE) % 3;
  }

  public boolean isRequiredManagement() {
    return !getManagementStatus().equals(ManagementStatus.NONE);
  }

  public int getAbsenceCount() {
    return attendanceStatusCounts.get(AttendanceStatus.ABSENCE);
  }

  public int getLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE);
  }

  public int getAttendanceCount() {
    return attendanceStatusCounts.get(AttendanceStatus.ATTENDANCE);
  }

  public ManagementStatus getManagementStatus() {
    return ManagementStatus.of(calculatePolicyAppliedAbsenceCount());
  }

  public Crew getCrew() {
    return crew;
  }

}
