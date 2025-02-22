package attendance.model.domain.crew;

import attendance.model.domain.log.TimeLogs;

public class CrewAttendanceStatus {

    private final Crew crew;
    private final AbsenceCount absenceCount;
    private final LateCount lateCount;
    private final AttendanceCount attendanceCount;
    private final ManagementStatus managementStatus;

    private CrewAttendanceStatus(Crew crew, AttendanceCount attendanceCount, AbsenceCount absenceCount, LateCount lateCount,
                                  ManagementStatus managementStatus) {
        this.crew = crew;
        this.attendanceCount = attendanceCount;
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.managementStatus = managementStatus;
    }

    public static CrewAttendanceStatus of(Crew crew, TimeLogs timeLogs) {
        AttendanceCount attendanceCount = timeLogs.calculateAttendanceCount();
        AbsenceCount absenceCount = timeLogs.calculateAbsenceCount();
        LateCount lateCount = timeLogs.calculateLateCount();
        ManagementStatus managementStatus = ManagementStatus.of(absenceCount, lateCount);
        return new CrewAttendanceStatus(crew, attendanceCount, absenceCount, lateCount, managementStatus);
    }

    public boolean requiresManagement() {
        return managementStatus.requiresManagement();
    }

    public int getPolicyAppliedAbsenceCount() {
        return absenceCount.getPolicyAppliedAbsenceCount(lateCount);
    }

    public int getPolicyAppliedLateCount() {
        return lateCount.calculatePolicyAppliedLateCount();
    }

    public Crew getCrew() {
        return crew;
    }

    public int getAbsenceCount() {
        return absenceCount.getValue();
    }

    public int getLateCount() {
        return lateCount.getValue();
    }

    public int getAttendanceCount() {
        return attendanceCount.getValue();
    }

    public ManagementStatus getManagementStatus() {
        return managementStatus;
    }
}
