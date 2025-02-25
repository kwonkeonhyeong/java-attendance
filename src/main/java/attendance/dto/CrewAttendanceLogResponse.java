package attendance.dto;

import attendance.model.domain.crew.ManagementStatus;
import attendance.model.domain.crew.Crew;
import java.util.List;

public class CrewAttendanceLogResponse {

  private final String crewName;
  private final int attendanceCount;
  private final int lateCount;
  private final int absenceCount;
  private final List<AttendanceLogResponse> timeLogs;
  private final String managementStatus;

  public CrewAttendanceLogResponse(String crewName, int attendanceCount, int lateCount,
      int absenceCount, List<AttendanceLogResponse> timeLogs, String managementStatus) {
    this.crewName = crewName;
    this.attendanceCount = attendanceCount;
    this.lateCount = lateCount;
    this.absenceCount = absenceCount;
    this.timeLogs = timeLogs;
    this.managementStatus = managementStatus;
  }

  public static CrewAttendanceLogResponse of(Crew crew, int attendanceCount,
      int lateCount, int absenceCount,
      List<AttendanceLogResponse> attendanceLogResponse,
      ManagementStatus managementStatus) {
    return new CrewAttendanceLogResponse(crew.getName(), attendanceCount,
        lateCount, absenceCount, attendanceLogResponse,
        managementStatus.getName());
  }

  public String getCrewName() {
    return crewName;
  }

  public int getAttendanceCount() {
    return attendanceCount;
  }

  public int getLateCount() {
    return lateCount;
  }

  public int getAbsenceCount() {
    return absenceCount;
  }

  public List<AttendanceLogResponse> getTimeLogs() {
    return timeLogs;
  }

  public String getManagementStatus() {
    return managementStatus;
  }

}
