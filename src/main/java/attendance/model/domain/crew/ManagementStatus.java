package attendance.model.domain.crew;

public enum ManagementStatus {

    EXPULSION("제적"),
    COUNSELING("면담"),
    WARNING("경고"),
    NONE("일반");

    private static final int MIN_ABSENCES_FOR_EXPULSION = 5;
    private static final int MIN_ABSENCES_FOR_COUNSELING = 2;
    private static final int MIN_ABSENCES_FOR_WARNING = 1;

    private final String name;

    ManagementStatus(String name) {
        this.name = name;
    }

    public static ManagementStatus of(final AbsenceCount absenceCount, final LateCount lateCount) {
        int realAbsenceCount = absenceCount.getPolicyAppliedAbsenceCount(lateCount);
        if (realAbsenceCount > MIN_ABSENCES_FOR_EXPULSION) {
            return EXPULSION;
        }
        if (realAbsenceCount > MIN_ABSENCES_FOR_COUNSELING) {
            return COUNSELING;
        }
        if (realAbsenceCount > MIN_ABSENCES_FOR_WARNING) {
            return WARNING;
        }
        return NONE;
    }

    public boolean requiresManagement() {
        return this != NONE;
    }

    public String getName() {
        return name;
    }

}
