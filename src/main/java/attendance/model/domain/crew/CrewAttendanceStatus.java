package attendance.model.domain.crew;

public class CrewAttendanceStatus {

  private final Crew crew;
  private final AttendanceStatusCounts attendanceStatusCounts;
  private final ManagementStatus managementStatus;

  private CrewAttendanceStatus(Crew crew, AttendanceStatusCounts attendanceStatusCounts, ManagementStatus managementStatus) {
    this.crew = crew;
    this.attendanceStatusCounts = attendanceStatusCounts;
    this.managementStatus = managementStatus;
  }

  public static CrewAttendanceStatus of(Crew crew, TimeLogs timeLogs) {
    AttendanceStatusCounts attendanceStatusCounts;
    return new CrewAttendanceStatus(
        crew,
        attendanceStatusCounts = AttendanceStatusCounts.of(timeLogs),
        ManagementStatus.of(attendanceStatusCounts.calculatePolicyAppliedAbsenceCount())
    );
  }

  public boolean isRequiredManagement() {
    return managementStatus.isRequiredManagement();
  }

  public Crew getCrew() {
    return crew;
  }

  public ManagementStatus getManagementStatus() {
    return managementStatus;
  }

  public int getPolicyAppliedAbsenceCount() {
    return attendanceStatusCounts.calculatePolicyAppliedAbsenceCount();
  }

  public int getRemainingLateCount() {
    return attendanceStatusCounts.calculateRemainingLateCount();
  }

  public int getAbsenceCount() {
    return attendanceStatusCounts.getAbsenceCount();
  }

  public int getLateCount() {
    return attendanceStatusCounts.getLateCount();
  }

}
