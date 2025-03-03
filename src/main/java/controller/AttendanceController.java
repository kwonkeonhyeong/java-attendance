package controller;

import attendance.AttendanceBook;
import attendance.AttendanceRecord;
import attendance.AttendanceStatus;
import attendance.Crew;
import attendance.ExistentAttendanceRecord;
import attendance.ManagementStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import view.InputView;
import view.OutputView;
import view.io.CrewAttendanceRecordInitializer;

public class AttendanceController {

  //  private static final LocalDate GLOBAL_DATE = LocalDate.now();
  public static final LocalDate GLOBAL_DATE = LocalDate.of(2024, 12, 13);

  private final InputView inputView;
  private final OutputView outputView;
  private final AttendanceBook attendanceBook = new AttendanceBook(
      new CrewAttendanceRecordInitializer().initialize());

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
    try {
      String nickname = inputView.printInputCrewNickName();
      LocalDateTime dateTime = inputView.printInputAttendanceTime();
      ExistentAttendanceRecord checkedAttendanceRecord = attendanceBook.check(nickname, dateTime);
      outputView.printCheckedAttendanceResult(checkedAttendanceRecord.getDateTime(),
          AttendanceStatus.from(checkedAttendanceRecord));
    } catch (RuntimeException exception) {
      System.out.println(exception.getMessage());
    }
  }

  public void modifyAttendance() {
    try {
      String nickName = inputView.printInputCrewNickName();
      int day = inputView.printInputModifyDate();
      LocalDate date = LocalDate.of(GLOBAL_DATE.getYear(), GLOBAL_DATE.getMonth(), day);
      LocalTime time = inputView.printInputModifyTime();
      Entry<AttendanceRecord, AttendanceRecord> modified = attendanceBook.modify(nickName,
          LocalDateTime.of(date, time));
      outputView.printModifiedAttendanceResult(modified.getKey().getRecord(), AttendanceStatus.from(modified.getKey()), modified.getValue().getRecord(), AttendanceStatus.from(modified.getValue()));
    } catch (RuntimeException exception) {
      System.out.println(exception.getMessage());
    }
  }

  public void searchAttendanceRecords() {
    try {
      String nickName = inputView.printInputCrewNickName();
      LinkedHashMap<LocalDateTime, AttendanceStatus> searched = attendanceBook.search(nickName,
          GLOBAL_DATE);
      Map<AttendanceStatus, Integer> attendanceResult = attendanceBook.calculateAttendanceResult(
          nickName, GLOBAL_DATE);
      ManagementStatus managementStatus = attendanceBook.checkManagementCrew(attendanceResult);
      outputView.printCrewAttendanceRecords(nickName,searched);
      outputView.printCrewAttendanceResult(attendanceResult, managementStatus);
    } catch (IllegalArgumentException exception) {
      System.out.println(exception.getMessage());
    }
  }

  public void checkManagementCrews() {
    Map<Crew, ManagementStatus> crewsManagementStatus = attendanceBook.checkManagementCrews(
        GLOBAL_DATE);
    Map<Crew, Map<AttendanceStatus, Integer>> crewsAttendanceResult = attendanceBook.calculateCrewsAttendanceResult(
        GLOBAL_DATE);
    outputView.printManagementCrews(crewsManagementStatus, crewsAttendanceResult);
  }

  public void quit() {
  }
}
