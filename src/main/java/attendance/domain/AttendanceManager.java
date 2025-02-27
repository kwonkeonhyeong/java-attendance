package attendance.domain;

import attendance.domain.Information.AttendanceInformation;
import attendance.domain.Information.AttendanceUpdatesInformation;
import attendance.domain.Information.CrewAttendanceInformation;
import attendance.domain.Information.ManagementCrewInformation;
import attendance.domain.crew.Crew;
import attendance.domain.crew.TimeLog;
import attendance.view.input.InputView;
import attendance.view.output.OutputView;
import java.time.LocalDateTime;
import java.util.List;

public class AttendanceManager {

  private final InputView inputView;
  private final OutputView outputView;
  private final AttendanceBook attendanceBook;

  public AttendanceManager() {
    this.inputView = new InputView();
    this.outputView = new OutputView();
    this.attendanceBook = new AttendanceBook(inputView.loadCrewAttendanceTimeLogs());
  }

  public void startAttendanceProcess() {
    boolean isAvailableCommand;
    do {
      isAvailableCommand = selectCommand();
    } while (isAvailableCommand);
  }

  private boolean selectCommand() {
    try {
      Command command = inputView.inputCommand();
      command.run(this);
      return command != Command.QUIT;
    } catch (IllegalArgumentException exception) {
      System.out.println(exception.getMessage());
      return true;
    }
  }

  public void attend() {
    try {
      String nickname = inputView.inputNickname();
      Crew crew = new Crew(nickname);
      TimeLog timeLog = TimeLog.from(inputView.inputAttendanceTime());
      attendanceBook.attend(crew, timeLog);
      outputView.printAttendanceLog(createAttendanceInformation(timeLog));
    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  private AttendanceInformation createAttendanceInformation(TimeLog timeLog) {
    return new AttendanceInformation(timeLog.getDateTime(),
        timeLog.judgeAttendanceStatus());
  }

  public void updateAttendanceTimeLog() {
    try {
      String crewName = inputView.inputUpdateCrewName();
      Crew crew = new Crew(crewName);
      LocalDateTime updatedTime = LocalDateTime.of(inputView.inputUpdateAttendanceDate(),
          inputView.inputUpdateAttendanceTime());
      AttendanceUpdatesInformation attendanceUpdatesInformation = attendanceBook.modifiedAttendanceTimeLog(
          crew, TimeLog.from(updatedTime));
      outputView.printAttendanceUpdatesInformation(attendanceUpdatesInformation);
    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  public void checkCrewAttendanceTimeLogs() {
    try {
      String crewName = inputView.inputNickname();
      Crew crew = new Crew(crewName);
      CrewAttendanceInformation crewAttendanceInformation = attendanceBook.checkAttendanceTimeLogs(
          crew);
      outputView.printCrewAttendanceInformation(crewAttendanceInformation);
    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  public void searchManagementCrews() {
    List<ManagementCrewInformation> managementCrewInformation = attendanceBook.checkManagementCrews();
    outputView.printManagementCrewInformation(managementCrewInformation);
  }

  public void quit() {
  }

}
