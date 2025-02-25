package attendance.controller;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.RequiresManagementCrewResponse;
import attendance.dto.UpdateAttendanceResponse;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.comprator.CrewAttendanceComparator;
import attendance.model.domain.crew.comprator.DefaultCrewAttendanceComparator;
import attendance.model.repository.AttendanceRepository;
import attendance.model.repository.CrewAttendanceDeserializer;
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
  private final CrewAttendanceDeserializer crewAttendanceDeserializer = new CrewAttendanceDeserializer();
  private final Path crewAttendanceDataPath = Path.of(FILE_PATH);
  private final AttendanceRepository attendanceRepository = new AttendanceRepository(
      crewAttendanceDeserializer,
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

      AttendanceLogResponse response = attendanceService.attendance(crew, attendanceTime);

      outputView.printAttendanceLog(response);
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

      UpdateAttendanceResponse updateAttendanceResponse = attendanceService.updateAttendance(crew,
          updatedTime);

      outputView.printUpdateAttendanceResponse(updateAttendanceResponse);

    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  public void checkCrewAttendance() {
    try {
      String crewName = inputView.inputNickname();

      outputView.printCrewAttendanceLogResponse(
          attendanceService.getAttendanceLog(crewName)
      );
    } catch (RuntimeException runtimeException) {
      System.out.println(runtimeException.getMessage());
    }
  }

  public void searchManagementCrews() {
    List<RequiresManagementCrewResponse> requiresManagementCrewResponses = attendanceService.getRequiresManagementCrews(
        crewAttendanceComparator);
    outputView.printManagementCrews(requiresManagementCrewResponses);
  }

  public void quit() {
  }

}
