package attendance.model.domain.crew;

import java.util.Objects;

public class AbsenceCount {

  private final int value;

  private AbsenceCount(int value) {
    validate(value);
    this.value = value;
  }

  public static AbsenceCount from(int value) {
    return new AbsenceCount(value);
  }

  public int getPolicyAppliedAbsenceCount(LateCount lateCount) {
    return value + lateCount.calculatePolicyAppliedAbsenceCount();
  }

  public int getValue() {
    return value;
  }

  private void validate(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("결석 횟수는 음수가 될 수 없습니다.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbsenceCount that = (AbsenceCount) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return "AbsenceCount{" +
        "value=" + value +
        '}';
  }

}
