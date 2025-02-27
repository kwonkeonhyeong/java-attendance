package attendance.domain.Information;

import attendance.domain.crew.ManagementStatus;
import attendance.domain.crew.Crew;
import java.util.List;

public class CrewAttendanceInformation {

  private final String crewName;
  private final long attendanceCount;
  private final long lateCount;
  private final long absenceCount;
  private final List<AttendanceInformation> attendanceInformation;
  private final ManagementStatus managementStatus;

  public CrewAttendanceInformation(String crewName, long attendanceCount, long lateCount,
      long absenceCount, List<AttendanceInformation> attendanceInformation,
      ManagementStatus managementStatus) {
    this.crewName = crewName;
    this.attendanceCount = attendanceCount;
    this.lateCount = lateCount;
    this.absenceCount = absenceCount;
    this.attendanceInformation = attendanceInformation;
    this.managementStatus = managementStatus;
  }

  public static CrewAttendanceInformation of(Crew crew, long attendanceCount,
      long lateCount, long absenceCount,
      List<AttendanceInformation> attendanceInformation,
      ManagementStatus managementStatus) {
    return new CrewAttendanceInformation(crew.getName(), attendanceCount,
        lateCount, absenceCount, attendanceInformation,
        managementStatus);
  }

  public String getCrewName() {
    return crewName;
  }

  public long getAttendanceCount() {
    return attendanceCount;
  }

  public long getLateCount() {
    return lateCount;
  }

  public long getAbsenceCount() {
    return absenceCount;
  }

  public List<AttendanceInformation> getAttendanceInformation() {
    return attendanceInformation;
  }

  public ManagementStatus getManagementStatus() {
    return managementStatus;
  }

}
