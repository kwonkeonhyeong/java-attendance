package attendance.view.input;

import java.util.Arrays;

public enum Command {

    ATTENDANCE_CHECK("1", "출석 확인"),
    ATTENDANCE_MODIFY("2", "출석 수정"),
    CREW_ATTENDANCE_CHECK("3", "크루별 출석 기록 확인"),
    EXPELLED_CREW_CHECK("4", "제적 위험자 확인"),
    QUIT("Q", "종료");

    private final String option;
    private final String description;

    Command(String option, String description) {
        this.option = option;
        this.description = description;
    }

    public static Command from(String option) {
        return Arrays.stream(values())
                .filter(command -> command.option.equals(option))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 기능은 없습니다."));
    }

    public String getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }
}
