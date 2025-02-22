package attendance.model.domain.crew;

import java.util.Objects;

public class LateCount {

    private final int value;

    private LateCount(int value) {
        validate(value);
        this.value = value;
    }

    public static LateCount from(int value) {
        return new LateCount(value);
    }

    public int calculatePolicyAppliedAbsenceCount() {
        return value / 3;
    }

    public int calculatePolicyAppliedLateCount() {
        return value % 3;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LateCount lateCount = (LateCount) o;
        return value == lateCount.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "LateCount{" +
            "value=" + value +
            '}';
    }

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("지각 횟수는 음수가 될 수 없습니다.");
        }
    }

}
