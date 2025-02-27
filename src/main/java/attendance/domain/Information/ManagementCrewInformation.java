package attendance.domain.Information;

import attendance.domain.crew.CrewAttendanceStatus;
import attendance.domain.crew.ManagementStatus;

public class ManagementCrewInformation {

    private final String crewName;
    private final long absenceCount;
    private final long lateCount;
    private final ManagementStatus managementStatus;

    private ManagementCrewInformation(String crewName, long absenceCount, long lateCount, ManagementStatus managementStatus) {
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
                crewAttendanceStatus.getManagementStatus()
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

    public ManagementStatus getManagementStatus() {
        return managementStatus;
    }

}
