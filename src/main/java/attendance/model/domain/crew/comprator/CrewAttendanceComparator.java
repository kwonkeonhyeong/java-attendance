package attendance.model.domain.crew.comprator;

import attendance.model.domain.crew.CrewAttendanceStatus;
import java.util.Comparator;

@FunctionalInterface
public interface CrewAttendanceComparator extends Comparator<CrewAttendanceStatus> {
}
