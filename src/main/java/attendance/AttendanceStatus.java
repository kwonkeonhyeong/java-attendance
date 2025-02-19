package attendance;

public class AttendanceStatus {

    private final int absenceCount;
    private final int lateCount;
    private final DangerStatus dangerStatus;

    private AttendanceStatus(int absenceCount, int lateCount, DangerStatus dangerStatus) {
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.dangerStatus = dangerStatus;
    }

    public static AttendanceStatus of(int absenceCount, int lateCount) {
        return new AttendanceStatus(absenceCount, lateCount, DangerStatus.of(absenceCount, lateCount));
    }

    public boolean isDanger() {
        return true;
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

    public DangerStatus getDangerStatus() {
        return dangerStatus;
    }
}
