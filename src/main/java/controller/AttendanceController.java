package controller;

import attendance.AttendanceBook;
import java.time.LocalDate;
import view.InputView;
import view.OutputView;

public class AttendanceController {

//  private static final LocalDate GLOBAL_DATE = LocalDate.now();
  private static final LocalDate GLOBAL_DATE = LocalDate.of(2024,12,14);

  private final InputView inputView;
  private final OutputView outputView;

  public AttendanceController(InputView inputView, OutputView outputView) {
    this.inputView = inputView;
    this.outputView = outputView;
  }

  public void run() {
    boolean isAvailableCommand;
    do {
      isAvailableCommand = selectCommand();
    } while (isAvailableCommand);
  }

  public boolean selectCommand() {
    try {
      Command command = Command.from(inputView.printCommand(GLOBAL_DATE));
      command.run(this);
      return command != Command.QUIT;
    } catch (IllegalArgumentException exception) {
      System.out.println(exception.getMessage());
      return true;
    }
  }

  public void checkAttendance() {
    System.out.println("출석 확인");
  }

  public void modifyAttendance() {
    System.out.println("출석 수정");
  }

  public void searchAttendanceRecords() {
    System.out.println("출석 조회");
  }

  public void checkManagementCrews() {
    System.out.println("제적 위험자 확인");
  }

  public void quit() {
  }
}
