package attendance.model.domain.crew;

import attendance.model.CrewAttendanceStatus;
import java.util.Comparator;

@FunctionalInterface
public interface CrewAttendanceComparator extends Comparator<CrewAttendanceStatus> {
}
