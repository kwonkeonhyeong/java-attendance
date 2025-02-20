package attendance.model;

import java.time.LocalDateTime;
import java.util.Collection;

public class CrewAttendanceStatus {

    private final int absenceCount;
    private final int lateCount;
    private final ManagementStatus managementStatus;

    private CrewAttendanceStatus(int absenceCount, int lateCount, ManagementStatus managementStatus) {
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.managementStatus = managementStatus;
    }

    public static CrewAttendanceStatus of(int absenceCount, int lateCount) {
        return new CrewAttendanceStatus(absenceCount, lateCount, ManagementStatus.of(absenceCount, lateCount));
    }

    public static CrewAttendanceStatus of(Collection<LocalDateTime> times) {
        int absenceCount = Math.toIntExact(
                times.stream()
                        .filter(time -> AttendanceStatus.from(time) == AttendanceStatus.ABSENCE)
                        .count()
        );
        int lateCount = Math.toIntExact(
                times.stream()
                        .filter(time -> AttendanceStatus.from(time) == AttendanceStatus.LATE)
                        .count()
        );

        return CrewAttendanceStatus.of(absenceCount, lateCount);
    }

    public boolean requiresManagement() {
        return managementStatus.requiresManagement();
    }

    public int getRealAbsenceCount() {
        return absenceCount + (lateCount / 3);
    }

    public int getRealLateCount() {
        return lateCount % 3;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public ManagementStatus getDangerStatus() {
        return managementStatus;
    }
}
