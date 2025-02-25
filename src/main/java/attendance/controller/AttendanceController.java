package attendance.controller;

import attendance.model.domain.Information.AttendanceInformation;
import attendance.model.domain.Information.ManagementCrewInformation;
import attendance.model.domain.Information.AttendanceUpdatesInformation;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.comprator.CrewAttendanceComparator;
import attendance.model.domain.crew.comprator.DefaultCrewAttendanceComparator;
import attendance.model.repository.AttendanceRepository;
import attendance.model.repository.CrewTimeLogsInitializer;
import attendance.model.repository.DefaultCrewTimeLogsInitializer;
import attendance.model.service.AttendanceService;
import attendance.view.input.InputView;
import attendance.view.output.OutputView;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class AttendanceController {

  private static final String FILE_PATH = "src/main/resources/attendances.csv";

  private final InputView inputView = new InputView();
  private final OutputView outputView = new OutputView();
  private final CrewTimeLogsInitializer crewTimeLogsInitializer = new DefaultCrewTimeLogsInitializer();
  private final Path crewAttendanceDataPath = Path.of(FILE_PATH);
  private final AttendanceRepository attendanceRepository = new AttendanceRepository(
      crewTimeLogsInitializer,
      crewAttendanceDataPath);
  private final AttendanceService attendanceService = new AttendanceService(attendanceRepository);
  private final CrewAttendanceComparator crewAttendanceComparator = new DefaultCrewAttendanceComparator();

  public void run() {
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

  public void attendance() {
    try {
      String crewName = inputView.inputNickname();
      Crew crew = attendanceService.findCrewByName(crewName);
      LocalDateTime attendanceTime = inputView.inputAttendanceTime();
      AttendanceInformation attendanceInformation = attendanceService.attendance(crew,
          attendanceTime);
      outputView.printAttendanceLog(attendanceInformation);
    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  public void updateAttendance() {
    try {
      String crewName = inputView.inputUpdateCrewName();
      Crew crew = attendanceService.findCrewByName(crewName);
      LocalDateTime updatedTime = LocalDateTime.of(inputView.inputUpdateAttendanceDate(),
          inputView.inputUpdateAttendanceTime());
      AttendanceUpdatesInformation attendanceUpdatesInformation = attendanceService.updateAttendance(
          crew, updatedTime);
      outputView.printAttendanceUpdatesInformation(attendanceUpdatesInformation);
    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  public void checkCrewAttendance() {
    try {
      String crewName = inputView.inputNickname();
      outputView.printCrewAttendanceInformation(
          attendanceService.generateCrewAttendanceInformation(crewName));
    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  public void searchManagementCrews() {
    List<ManagementCrewInformation> managementCrewInformation = attendanceService.generateManagementCrewInformation(
        crewAttendanceComparator);
    outputView.printManagementCrewInformation(managementCrewInformation);
  }

  public void quit() {
  }

}
