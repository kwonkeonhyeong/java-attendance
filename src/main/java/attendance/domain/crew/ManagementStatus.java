package attendance.domain.crew;

public enum ManagementStatus {

  EXPULSION,
  COUNSELING,
  WARNING,
  NONE;

  private static final int MIN_ABSENCES_FOR_EXPULSION = 5;
  private static final int MIN_ABSENCES_FOR_COUNSELING = 2;
  private static final int MIN_ABSENCES_FOR_WARNING = 1;

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

}
