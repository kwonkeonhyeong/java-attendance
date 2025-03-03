package controller;

import java.util.Arrays;
import java.util.function.Consumer;

public enum Command {

  CHECK_ATTENDANCE("1", AttendanceController::checkAttendance),
  MODIFY_ATTENDANCE("2", AttendanceController::modifyAttendance),
  SEARCH_ALL_ATTENDANCE("3", AttendanceController::searchAttendanceRecords),
  CHECK_MANAGEMENT_CREWS("4", AttendanceController::checkManagementCrews),
  QUIT("Q", AttendanceController::quit);

  private final String option;
  private final Consumer<AttendanceController> command;

  Command(String option, Consumer<AttendanceController> command) {
    this.option = option;
    this.command = command;
  }

  public static Command from(String input) {
    return Arrays.stream(Command.values())
        .filter(command -> command.option.equals(input))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("해당 기능은 존재하지 않습니다."));
  }

  public void run(AttendanceController controller) {
    command.accept(controller);
  }
}
