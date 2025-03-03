package attendance;

public enum ManagementStatus {
  EXPULSION, INTERVIEW, WARNING, GENERAL;

  private static final int EXPULSION_CONDITION = 5;
  private static final int INTERVIEW_CONDITION = 2;

  public static ManagementStatus from(int absenceCont) {
    if (absenceCont > EXPULSION_CONDITION) {
      return EXPULSION;
    }
    if (absenceCont > INTERVIEW_CONDITION) {
      return INTERVIEW;
    }
    if (absenceCont == INTERVIEW_CONDITION) {
      return WARNING;
    }
    return GENERAL;
  }
}
