package attendance.controller;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.UpdateAttendanceResponse;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.CrewAttendanceComparator;
import attendance.model.domain.crew.DefaultCrewAttendanceComparator;
import attendance.model.repository.AttendanceRepository;
import attendance.model.repository.CrewAttendanceDeserializer;
import attendance.model.service.AttendanceService;
import attendance.view.input.Command;
import attendance.view.input.InputView;
import attendance.view.output.OutputView;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Controller {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    private final CrewAttendanceDeserializer crewAttendanceDeserializer = new CrewAttendanceDeserializer();
    private final Path crewAttendanceDataPath = Path.of("src/main/resources/attendances.csv");
    private final AttendanceRepository attendanceRepository = new AttendanceRepository(crewAttendanceDeserializer,
            crewAttendanceDataPath);
    private final AttendanceService attendanceService = new AttendanceService(attendanceRepository);
    private final CrewAttendanceComparator crewAttendanceComparator = new DefaultCrewAttendanceComparator();

    public void run() {

        Command command;
        do {
            command = inputView.inputCommand();

            if (command == Command.ATTENDANCE_CHECK) {
                attendance();
            }

            if (command == Command.ATTENDANCE_MODIFY) {
                updateAttendance();
            }

            if (command == Command.CREW_ATTENDANCE_CHECK) {
                checkCrewAttendance();
            }
        } while (command != Command.QUIT);

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
            String crewName = inputView.inputNickname();
            Crew crew = attendanceService.findCrewByName(crewName);

            LocalDateTime updatedTime = LocalDateTime.of(inputView.inputUpdateAttendanceDate(),
                    inputView.inputUpdateAttendanceTime());

            UpdateAttendanceResponse updateAttendanceResponse = attendanceService.updateAttendance(crew, updatedTime);

            outputView.printUpdateAttendanceResponse(updateAttendanceResponse);

        } catch (RuntimeException runtimeException) {
            System.out.println(runtimeException.getMessage());
        }
    }

    public void checkCrewAttendance() {
        try {
            String crewName = inputView.inputNickname();
            Crew crew = attendanceService.findCrewByName(crewName);

            outputView.printCrewAttendanceLogResponse(attendanceService.getAttendanceLog(crew));
        } catch (RuntimeException runtimeException) {
            System.out.println(runtimeException.getMessage());
        }
    }
}
