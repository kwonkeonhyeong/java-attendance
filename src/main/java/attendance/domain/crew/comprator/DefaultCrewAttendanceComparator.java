package attendance.domain.crew.comprator;

import attendance.domain.crew.CrewAttendanceStatus;
import java.util.Comparator;

public class DefaultCrewAttendanceComparator implements CrewAttendanceComparator {

  @Override
  public int compare(CrewAttendanceStatus o1, CrewAttendanceStatus o2) {
    return Comparator
        .comparing(CrewAttendanceStatus::getPolicyAppliedAbsenceCount, Comparator.reverseOrder())
        .thenComparing(CrewAttendanceStatus::getRemainingLateCount, Comparator.reverseOrder())
        .thenComparing(crewAttendanceStatus -> crewAttendanceStatus.getCrew().getName())
        .compare(o1, o2);
  }
}
