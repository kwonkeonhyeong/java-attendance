package attendance.model.domain.crew;

import java.util.Objects;

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
      throw new IllegalArgumentException("출석 횟수는 음수가 될 수 없습니다");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttendanceCount that = (AttendanceCount) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return "AttendanceCount{" +
        "value=" + value +
        '}';
  }

}
