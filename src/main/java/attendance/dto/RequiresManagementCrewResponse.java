package attendance.dto;

import attendance.model.domain.crew.CrewAttendanceStatus;

public class RequiresManagementCrewResponse {
    private final String crewName;
    private final int absenceCount;
    private final int lateCount;
    private final String managementStatus;

    private RequiresManagementCrewResponse(String crewName, int absenceCount, int lateCount, String managementStatus) {
        this.crewName = crewName;
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.managementStatus = managementStatus;
    }

    public static RequiresManagementCrewResponse from(CrewAttendanceStatus crewAttendanceStatus) {
        return new RequiresManagementCrewResponse(
                crewAttendanceStatus.getCrew().getName(),
                crewAttendanceStatus.getAbsenceCount(),
                crewAttendanceStatus.getLateCount(),
                crewAttendanceStatus.getManagementStatus().getName()
        );
    }

    public String getCrewName() {
        return crewName;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public String getManagementStatus() {
        return managementStatus;
    }

}
