package attendance;

public enum DangerStatus {
    EXPULSION("제적"),
    COUNSELING("면담"),
    WARNING("경고"),
    NONE("일반");

    private final String name;

    DangerStatus(String name) {
        this.name = name;
    }

    public static DangerStatus of(final int absenceCount, final int lateCount) {
        int realAbsenceCount = getRealAbsenceCount(absenceCount, lateCount);
        if (realAbsenceCount > 5) {
            return EXPULSION;
        }
        if (realAbsenceCount > 2) {
            return COUNSELING;
        }
        if (realAbsenceCount > 1) {
            return WARNING;
        }
        return NONE;
    }

    public static int getRealAbsenceCount(final int absenceCount, final int lateCount) {
        return absenceCount + (lateCount / 3);
    }

    public boolean isDanger() {
        return this != NONE;
    }

    public String getName() {
        return name;
    }
}
