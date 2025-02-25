package attendance.controller;

import java.util.Arrays;
import java.util.function.Consumer;

public enum Command {

    ATTENDANCE_CHECK("1", "출석 확인", AttendanceController::attendance),
    ATTENDANCE_MODIFY("2", "출석 수정", AttendanceController::updateAttendance),
    CREW_ATTENDANCE_CHECK("3", "크루별 출석 기록 확인", AttendanceController::checkCrewAttendance),
    EXPULSION_CREW_CHECK("4", "제적 위험자 확인", AttendanceController::searchManagementCrews),
    QUIT("Q", "종료", AttendanceController::quit);

    private final String option;
    private final String description;
    private final Consumer<AttendanceController> command;

    Command(String option, String description, Consumer<AttendanceController> command) {
        this.option = option;
        this.description = description;
        this.command = command;
    }

    public static Command from(String option) {
        return Arrays.stream(values())
                .filter(command -> command.option.equals(option))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 기능은 없습니다."));
    }

    public void run(AttendanceController controller) {
        command.accept(controller);
    }

    public String getOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

}
