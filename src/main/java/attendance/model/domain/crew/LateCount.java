package attendance.model.domain.crew;

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

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("지각 횟수는 음수가 될 수 없습니다.");
        }
    }
}
