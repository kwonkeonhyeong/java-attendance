package attendance.model.domain.crew;

import attendance.model.CrewAttendance;
import java.util.Comparator;

@FunctionalInterface
public interface CrewAttendanceComparator extends Comparator<CrewAttendance> {
}
