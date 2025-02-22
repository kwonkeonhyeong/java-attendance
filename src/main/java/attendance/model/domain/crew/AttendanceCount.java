package attendance.model.domain.crew;

public class AttendanceCount {

  private final int value;

  private AttendanceCount(int value) {
    validate(value);
    this.value = value;
  }

  public static AttendanceCount from(int value) {
    return new AttendanceCount(value);
  }

  public int getValue() {
    return value;
  }

  private void validate(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("출석 횟수는 음수가 될 수 없습니다.");
    }
  }
}
