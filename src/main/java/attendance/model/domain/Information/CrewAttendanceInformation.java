package attendance.model.domain.Information;

import attendance.model.domain.crew.ManagementStatus;
import attendance.model.domain.crew.Crew;
import java.util.List;

public class CrewAttendanceInformation {

  private final String crewName;
  private final int attendanceCount;
  private final int lateCount;
  private final int absenceCount;
  private final List<AttendanceInformation> attendanceInformation;
  private final String managementStatus;

  public CrewAttendanceInformation(String crewName, int attendanceCount, int lateCount,
      int absenceCount, List<AttendanceInformation> attendanceInformation, String managementStatus) {
    this.crewName = crewName;
    this.attendanceCount = attendanceCount;
    this.lateCount = lateCount;
    this.absenceCount = absenceCount;
    this.attendanceInformation = attendanceInformation;
    this.managementStatus = managementStatus;
  }

  public static CrewAttendanceInformation of(Crew crew, int attendanceCount,
      int lateCount, int absenceCount,
      List<AttendanceInformation> attendanceInformation,
      ManagementStatus managementStatus) {
    return new CrewAttendanceInformation(crew.getName(), attendanceCount,
        lateCount, absenceCount, attendanceInformation,
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

  public List<AttendanceInformation> getAttendanceInformation() {
    return attendanceInformation;
  }

  public String getManagementStatus() {
    return managementStatus;
  }

}
