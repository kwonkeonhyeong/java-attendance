package attendance.model.domain.crew;

import java.util.Objects;

public class LateCount {

    private static final int POLICY_COUNT = 3;
    private static final String INVALID_RANGE_MESSAGE = "지각 횟수는 음수가 될 수 없습니다";

    private final int value;

    private LateCount(int value) {
        validate(value);
        this.value = value;
    }

    public static LateCount from(int value) {
        return new LateCount(value);
    }

    public int calculatePolicyAppliedAbsenceCount() {
        return value / POLICY_COUNT;
    }

    public int calculatePolicyAppliedLateCount() {
        return value % POLICY_COUNT;
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
            throw new IllegalArgumentException(INVALID_RANGE_MESSAGE);
        }
    }

}
