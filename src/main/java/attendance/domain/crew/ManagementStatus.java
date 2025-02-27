package attendance.domain.crew;

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

    public static ManagementStatus of(long appliedPolicyAbsenceCount) {
        if (appliedPolicyAbsenceCount > MIN_ABSENCES_FOR_EXPULSION) {
            return EXPULSION;
        }
        if (appliedPolicyAbsenceCount > MIN_ABSENCES_FOR_COUNSELING) {
            return COUNSELING;
        }
        if (appliedPolicyAbsenceCount > MIN_ABSENCES_FOR_WARNING) {
            return WARNING;
        }
        return NONE;
    }

    public boolean isRequiredManagement() {
        return this != NONE;
    }

    public String getName() {
        return name;
    }

}
