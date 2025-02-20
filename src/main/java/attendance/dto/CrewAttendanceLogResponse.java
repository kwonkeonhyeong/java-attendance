package attendance.dto;

import attendance.model.domain.crew.Crew;
import java.util.List;

public class CrewAttendanceLogResponse {

    private final String crewName;
    private final List<AttendanceLogResponse> timeLogs;

    private CrewAttendanceLogResponse(String crewName, List<AttendanceLogResponse> timeLogs) {
        this.crewName = crewName;
        this.timeLogs = timeLogs;
    }

    public static CrewAttendanceLogResponse of(Crew crew, List<AttendanceLogResponse> attendanceLogRespons) {
        return new CrewAttendanceLogResponse(crew.getName(), attendanceLogRespons);
    }

    public String getCrewName() {
        return crewName;
    }

    public List<AttendanceLogResponse> getTimeLogs() {
        return timeLogs;
    }
}
