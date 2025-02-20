package attendance.model;

import attendance.model.domain.crew.AbsenceCount;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.LateCount;
import java.time.LocalDateTime;
import java.util.List;

public class CrewAttendanceStatus {

    private final Crew crew;
    private final AbsenceCount absenceCount;
    private final LateCount lateCount;
    private final int attendanceCount;
    private final ManagementStatus managementStatus;

    private CrewAttendanceStatus(Crew crew, AbsenceCount absenceCount, LateCount lateCount,
                                 int attendanceCount, ManagementStatus managementStatus) {
        this.crew = crew;
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.attendanceCount = attendanceCount;
        this.managementStatus = managementStatus;
    }

    public static CrewAttendanceStatus of(Crew crew, List<LocalDateTime> dateTimes) {

        AbsenceCount absenceCount = calculateAbsenceCount(dateTimes);
        LateCount lateCount = calculateLateCount(dateTimes);
        ManagementStatus managementStatus = ManagementStatus.of(absenceCount, lateCount);

        return new CrewAttendanceStatus(crew, absenceCount, lateCount,
                dateTimes.size() - absenceCount.getValue() - lateCount.getValue(), managementStatus);
    }

    private static AbsenceCount calculateAbsenceCount(List<LocalDateTime> dateTimes) {
        int value = Math.toIntExact(
                dateTimes.stream()
                        .filter(dateTime -> AttendanceStatus.from(dateTime) == AttendanceStatus.ABSENCE)
                        .count()
        );
        return AbsenceCount.from(value);
    }

    private static LateCount calculateLateCount(List<LocalDateTime> dateTimes) {
        int value = Math.toIntExact(
                dateTimes.stream()
                        .filter(dateTime -> AttendanceStatus.from(dateTime) == AttendanceStatus.LATE)
                        .count()
        );
        return LateCount.from(value);
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
        return attendanceCount;
    }

    public ManagementStatus getManagementStatus() {
        return managementStatus;
    }
}
