package attendance;

import java.time.LocalDateTime;
import java.util.List;

public class AttendanceLogResponse {

    private final String crewName;
    private final List<LocalDateTime> timeLogs;

    private AttendanceLogResponse(String crewName, List<LocalDateTime> timeLogs) {
        this.crewName = crewName;
        this.timeLogs = timeLogs;
    }

    public static AttendanceLogResponse of(Crew crew, List<LocalDateTime> timeLogs) {
        return new AttendanceLogResponse(
                crew.getName(),
                timeLogs
        );
    }

    public String getCrewName() {
        return crewName;
    }

    public List<LocalDateTime> getTimeLogs() {
        return timeLogs;
    }
}
