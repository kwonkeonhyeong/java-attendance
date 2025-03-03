package attendance;

public enum ManagementStatus {
  EXPULSION, INTERVIEW, WARNING, GENERAL;

  public static ManagementStatus from(int absenceCont) {
    if (absenceCont > 5) {
      return EXPULSION;
    }
    if (absenceCont > 2) {
      return INTERVIEW;
    }
    if (absenceCont == 2) {
      return WARNING;
    }
    return GENERAL;
  }
}
