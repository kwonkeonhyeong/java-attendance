package attendance.domain;

import attendance.domain.Information.AttendanceUpdatesInformation;
import attendance.domain.Information.CrewAttendanceInformation;
import attendance.domain.Information.ManagementCrewInformation;
import attendance.domain.crew.CrewAttendanceStatus;
import attendance.domain.crew.TimeLogs;
import attendance.domain.crew.TimeLog;
import attendance.domain.crew.Crew;
import attendance.domain.crew.comprator.DefaultCrewAttendanceComparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendanceBook {

  private static final String NON_EXISTS_CREW_MESSAGE = "존재하지 않는 크루입니다";

  private final Map<Crew, TimeLogs> crewAttendanceTimeLogs;

  public AttendanceBook(Map<Crew, TimeLogs> crewAttendanceTimeLogs) {
    this.crewAttendanceTimeLogs = crewAttendanceTimeLogs;
  }

  public TimeLog attend(Crew crew, TimeLog timeLog) {
    validateCrewExistence(crew);
    TimeLogs timeLogs = crewAttendanceTimeLogs.computeIfAbsent(crew,
        key -> new TimeLogs(new ArrayList<>()));
    timeLogs.add(timeLog);
    return timeLog;
  }

  public AttendanceUpdatesInformation modifiedAttendanceTimeLog(Crew crew, TimeLog updateTimeLog) {
    validateCrewExistence(crew);
    TimeLogs timeLogs = crewAttendanceTimeLogs.get(crew);
    return timeLogs.modify(updateTimeLog);
  }

  public CrewAttendanceInformation checkAttendanceTimeLogs(Crew crew) {
    validateCrewExistence(crew);
    TimeLogs timeLogs = crewAttendanceTimeLogs.get(crew);
    return timeLogs.generateCrewAttendanceInformation(crew);
  }

  public List<ManagementCrewInformation> checkManagementCrews() {
    return crewAttendanceTimeLogs.entrySet().stream()
        .map(crewAndTimeLogs -> CrewAttendanceStatus.of(crewAndTimeLogs.getKey(),
            crewAndTimeLogs.getValue()))
        .sorted(new DefaultCrewAttendanceComparator())
        .filter(CrewAttendanceStatus::isRequiredManagement)
        .map(ManagementCrewInformation::from)
        .toList();
  }

  private void validateCrewExistence(Crew crew) {
    if (!existsByCrew(crew)) {
      throw new IllegalArgumentException(NON_EXISTS_CREW_MESSAGE);
    }
  }

  private boolean existsByCrew(Crew crew) {
    return crewAttendanceTimeLogs.containsKey(crew);
  }

}
