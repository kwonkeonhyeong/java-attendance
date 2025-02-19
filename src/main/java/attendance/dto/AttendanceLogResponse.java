package attendance.dto;

import attendance.model.AttendanceAnalyzer;
import attendance.model.Calender;
import attendance.model.crew.Crew;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class AttendanceLogResponse {

    private final String crewName;
    private final List<TimeLogResponse> timeLogs;

    private AttendanceLogResponse(String crewName, List<TimeLogResponse> timeLogs) {
        this.crewName = crewName;
        this.timeLogs = timeLogs;
    }

    public static AttendanceLogResponse of(Crew crew, List<LocalDateTime> timeLogs,
                                           AttendanceAnalyzer attendanceAnalyzer) {
        List<LocalDate> dateLogs = timeLogs.stream()
                .map(LocalDateTime::toLocalDate)
                .toList();

        List<LocalDate> notExistsDatesBeforeToday = Calender.getNotExistsDatesBeforeToday(dateLogs);

        List<TimeLogResponse> existTimeLogs = timeLogs.stream()
                .map(timeLog -> TimeLogResponse.of(timeLog, attendanceAnalyzer))
                .toList();

        List<TimeLogResponse> notExistTimeLogs = notExistsDatesBeforeToday.stream()
                .map(TimeLogResponse::of)
                .toList();

        List<TimeLogResponse> timeLogResponses = Stream.concat(existTimeLogs.stream(), notExistTimeLogs.stream())
                .sorted(Comparator.comparing(TimeLogResponse::getDate))
                .toList();

        return new AttendanceLogResponse(crew.getName(), timeLogResponses);
    }

    public String getCrewName() {
        return crewName;
    }

    public List<TimeLogResponse> getTimeLogs() {
        return timeLogs;
    }
}
