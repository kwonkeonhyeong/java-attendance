package attendance.model.domain.crew;

import attendance.model.CrewAttendance;

public class DefaultCrewAttendanceComparator implements CrewAttendanceComparator {

    @Override
    public int compare(CrewAttendance o1, CrewAttendance o2) {
        if (o1.getAttendanceStatus().getRealAbsenceCount() == o2.getAttendanceStatus().getRealAbsenceCount()) {
            if (o1.getAttendanceStatus().getRealLateCount() == o2.getAttendanceStatus().getRealLateCount()) {
                return o1.getCrew().getName().compareTo(o2.getCrew().getName());
            }
            return o2.getAttendanceStatus().getRealLateCount() - o1.getAttendanceStatus().getRealLateCount();
        }
        return o2.getAttendanceStatus().getRealAbsenceCount() - o1.getAttendanceStatus().getRealAbsenceCount();
    }
}
