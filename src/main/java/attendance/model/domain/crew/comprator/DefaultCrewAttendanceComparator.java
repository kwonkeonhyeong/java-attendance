package attendance.model.domain.crew.comprator;

import attendance.model.domain.crew.CrewAttendanceStatus;

public class DefaultCrewAttendanceComparator implements CrewAttendanceComparator {

    @Override
    public int compare(CrewAttendanceStatus o1, CrewAttendanceStatus o2) {
        if (o1.getPolicyAppliedAbsenceCount() == o2.getPolicyAppliedAbsenceCount()) {
            if (o1.getRemainingLateCount() == o2.getRemainingLateCount()) {
                return o1.getCrew().getName().compareTo(o2.getCrew().getName());
            }
            return o2.getRemainingLateCount() - o1.getRemainingLateCount();
        }
        return o2.getPolicyAppliedAbsenceCount() - o1.getPolicyAppliedAbsenceCount();
    }

}
