package attendance.model.service;

import attendance.dto.AttendanceEditResponse;
import attendance.dto.AttendanceLogResponse;
import attendance.dto.DangerCrewResponse;
import attendance.dto.TimeLogResponse;
import attendance.model.AttendanceAnalyzer;
import attendance.model.Calender;
import attendance.model.CrewAttendance;
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
    private final AttendanceAnalyzer attendanceAnalyzer;
    private final CrewAttendanceComparator crewAttendanceComparator;

    public AttendanceService(AttendanceRepository attendanceRepository, AttendanceAnalyzer attendanceAnalyzer,
                             CrewAttendanceComparator crewAttendanceComparator) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceAnalyzer = attendanceAnalyzer;
        this.crewAttendanceComparator = crewAttendanceComparator;
    }

    public AttendanceEditResponse updateAttendance(Crew crew, LocalDateTime updatedTime) {
        LocalDateTime previousTime = attendanceRepository.findDateTimeByCrewAndDate(crew, updatedTime.toLocalDate())
                .orElseThrow(() -> new IllegalArgumentException("해당 크루는 해당 일자의 출석 기록이 없습니다."));

        attendanceRepository.update(crew, previousTime, updatedTime);

        return new AttendanceEditResponse(previousTime, updatedTime);
    }

    public AttendanceLogResponse getAttendanceLog(Crew crew) {
        List<LocalDateTime> attendanceLogs = attendanceRepository.findByCrew(crew);
        List<TimeLogResponse> existTimeLogs = makeExistsTimeLogResponses(attendanceLogs);
        List<TimeLogResponse> notExistTimeLogs = makeNoneExistsTimeLogResponses(attendanceLogs);
        List<TimeLogResponse> timeLogResponses = Stream.concat(existTimeLogs.stream(), notExistTimeLogs.stream())
                .sorted(Comparator.comparing(TimeLogResponse::getDate))
                .toList();
        return AttendanceLogResponse.of(crew, timeLogResponses);
    }

    private List<TimeLogResponse> makeExistsTimeLogResponses(List<LocalDateTime> attendanceLogs) {
        return attendanceLogs.stream()
                .map(timeLog -> TimeLogResponse.of(timeLog, attendanceAnalyzer))
                .toList();
    }

    private List<TimeLogResponse> makeNoneExistsTimeLogResponses(List<LocalDateTime> attendanceLogs) {
        List<LocalDate> dateLogs = attendanceLogs.stream()
                .map(LocalDateTime::toLocalDate)
                .toList();

        List<LocalDate> notExistsDatesBeforeToday = Calender.getNotExistsDatesBeforeToday(dateLogs);

        return notExistsDatesBeforeToday.stream()
                .map(TimeLogResponse::of)
                .toList();
    }

    public List<DangerCrewResponse> getDangerCrews() {
        return getSortedDangerCrewAttendance().stream()
                .map(DangerCrewResponse::from)
                .toList();
    }

    private List<CrewAttendance> getSortedDangerCrewAttendance() {
        return attendanceRepository.findAllCrews().stream()
                .map(crew -> CrewAttendance.of(crew, attendanceRepository.findByCrew(crew), attendanceAnalyzer))
                .filter(CrewAttendance::requiresManagement)
                .sorted(crewAttendanceComparator)
                .toList();
    }

}
