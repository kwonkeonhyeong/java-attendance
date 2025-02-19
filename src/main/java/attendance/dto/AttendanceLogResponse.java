package attendance.dto;

import attendance.model.domain.crew.Crew;
import java.util.List;

public class AttendanceLogResponse {

    private final String crewName;
    private final List<TimeLogResponse> timeLogs;

    private AttendanceLogResponse(String crewName, List<TimeLogResponse> timeLogs) {
        this.crewName = crewName;
        this.timeLogs = timeLogs;
    }

    public static AttendanceLogResponse of(Crew crew, List<TimeLogResponse> timeLogResponses) {
        return new AttendanceLogResponse(crew.getName(), timeLogResponses);
    }

    public String getCrewName() {
        return crewName;
    }

    public List<TimeLogResponse> getTimeLogs() {
        return timeLogs;
    }
}
