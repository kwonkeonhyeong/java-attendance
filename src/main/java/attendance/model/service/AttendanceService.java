package attendance.model.service;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.CrewAttendanceLogResponse;
import attendance.dto.RequiresManagementCrewResponse;
import attendance.dto.UpdateAttendanceResponse;
import attendance.model.AttendanceStatus;
import attendance.model.Calender;
import attendance.model.CrewAttendanceStatus;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.CrewAttendanceComparator;
import attendance.model.repository.AttendanceRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final CrewAttendanceComparator crewAttendanceComparator;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             CrewAttendanceComparator crewAttendanceComparator) {
        this.attendanceRepository = attendanceRepository;
        this.crewAttendanceComparator = crewAttendanceComparator;
    }

    public UpdateAttendanceResponse updateAttendance(Crew crew, LocalDateTime updatedTime) {
        LocalDateTime previousTime = attendanceRepository.findDateTimeByCrewAndDate(crew, updatedTime.toLocalDate())
                .orElseThrow(() -> new IllegalArgumentException("해당 크루는 해당 일자의 출석 기록이 없습니다."));

        attendanceRepository.update(crew, previousTime, updatedTime);

        return new UpdateAttendanceResponse(previousTime, updatedTime);
    }

    public CrewAttendanceLogResponse getAttendanceLog(Crew crew) {
        List<LocalDateTime> attendanceLogs = attendanceRepository.findByCrew(crew);
        List<AttendanceLogResponse> existTimeLogs = makeExistsTimeLogResponses(attendanceLogs);
        List<AttendanceLogResponse> notExistTimeLogs = makeNoneExistsTimeLogResponses(attendanceLogs);
        List<AttendanceLogResponse> attendanceLogResponses = Stream.concat(existTimeLogs.stream(),
                        notExistTimeLogs.stream())
                .sorted(Comparator.comparing(AttendanceLogResponse::getDate))
                .toList();
        return CrewAttendanceLogResponse.of(crew, attendanceLogResponses);
    }

    private List<AttendanceLogResponse> makeExistsTimeLogResponses(List<LocalDateTime> attendanceLogs) {
        return attendanceLogs.stream()
                .map(dateTime -> AttendanceLogResponse.of(dateTime, AttendanceStatus.from(dateTime)))
                .toList();
    }

    private List<AttendanceLogResponse> makeNoneExistsTimeLogResponses(List<LocalDateTime> attendanceLogs) {
        List<LocalDate> dateLogs = attendanceLogs.stream()
                .map(LocalDateTime::toLocalDate)
                .toList();

        List<LocalDate> notExistsDatesBeforeToday = Calender.getNotExistsDatesBeforeToday(dateLogs);

        return notExistsDatesBeforeToday.stream()
                .map(date -> AttendanceLogResponse.of(date, AttendanceStatus.ABSENCE))
                .toList();
    }

    public List<RequiresManagementCrewResponse> getRequiresManagementCrews() {
        return getSortedCrewAttendance().stream()
                .filter(CrewAttendanceStatus::requiresManagement)
                .map(RequiresManagementCrewResponse::from)
                .toList();
    }

    private List<CrewAttendanceStatus> getSortedCrewAttendance() {
        return attendanceRepository.findAllCrews().stream()
                .map(crew -> CrewAttendanceStatus.of(crew, attendanceRepository.findByCrew(crew)))
                .sorted(crewAttendanceComparator)
                .toList();
    }

}
