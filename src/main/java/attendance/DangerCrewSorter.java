package attendance;

public class DangerCrewSorter {

    public int compare(CrewAttendance o1, CrewAttendance o2) {
        if (o1.getAttendanceStatus().getAbsenceCount() == o2.getAttendanceStatus().getAbsenceCount()) {
            if (o1.getAttendanceStatus().getRealLateCount() == o2.getAttendanceStatus().getRealLateCount()) {
                return o2.getCrew().getName().compareTo(o1.getCrew().getName());
            }
            return o2.getAttendanceStatus().getRealLateCount() - o1.getAttendanceStatus().getRealLateCount();
        }
        return o2.getAttendanceStatus().getRealAbsenceCount() - o1.getAttendanceStatus().getRealAbsenceCount();
    }
}
