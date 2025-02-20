package attendance.dto;

import attendance.model.AttendanceStatus;
import attendance.model.ManagementStatus;
import attendance.model.domain.crew.Crew;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrewAttendanceLogResponse {

    private final String crewName;
    private final List<AttendanceLogResponse> timeLogs;
    private final String managementStatus;

    private CrewAttendanceLogResponse(String crewName, List<AttendanceLogResponse> timeLogs, String managementStatus) {
        this.crewName = crewName;
        this.timeLogs = timeLogs;
        this.managementStatus = managementStatus;
    }

    public static CrewAttendanceLogResponse of(Crew crew, List<AttendanceLogResponse> attendanceLogResponse,
                                               ManagementStatus managementStatus) {
        return new CrewAttendanceLogResponse(crew.getName(), attendanceLogResponse, managementStatus.getName());
    }

    public String getCrewName() {
        return crewName;
    }

    public List<AttendanceLogResponse> getTimeLogs() {
        return timeLogs;
    }

    public String getManagementStatus() {
        return managementStatus;
    }

    public Map<String, Integer> getAttendanceStatusStatistics() {
        return Arrays.stream(AttendanceStatus.values())
                .collect(Collectors.toMap(
                        AttendanceStatus::getName,
                        status -> (int) timeLogs.stream()
                                .filter(timeLog -> AttendanceStatus.from(
                                        timeLog.getAttendanceStatusResponse().getAttendanceStatus()) == status)
                                .count(),
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
