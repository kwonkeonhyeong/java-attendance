package attendance.domain.Information;

import attendance.domain.crew.CrewAttendanceStatus;

public class ManagementCrewInformation {

    private final String crewName;
    private final long absenceCount;
    private final long lateCount;
    private final String managementStatus;

    private ManagementCrewInformation(String crewName, long absenceCount, long lateCount, String managementStatus) {
        this.crewName = crewName;
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.managementStatus = managementStatus;
    }

    public static ManagementCrewInformation from(CrewAttendanceStatus crewAttendanceStatus) {
        return new ManagementCrewInformation(
                crewAttendanceStatus.getCrew().getName(),
                crewAttendanceStatus.getAbsenceCount(),
                crewAttendanceStatus.getLateCount(),
                crewAttendanceStatus.getManagementStatus().getName()
        );
    }

    public String getCrewName() {
        return crewName;
    }

    public long getAbsenceCount() {
        return absenceCount;
    }

    public long getLateCount() {
        return lateCount;
    }

    public String getManagementStatus() {
        return managementStatus;
    }

}
