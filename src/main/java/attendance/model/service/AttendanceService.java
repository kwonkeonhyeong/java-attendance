package attendance.model.service;

import attendance.model.domain.Information.AttendanceInformation;
import attendance.model.domain.Information.CrewAttendanceInformation;
import attendance.model.domain.Information.ManagementCrewInformation;
import attendance.model.domain.Information.AttendanceUpdatesInformation;
import attendance.model.domain.crew.CrewAttendanceStatus;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.comprator.CrewAttendanceComparator;
import attendance.model.domain.crew.TimeLog;
import attendance.model.domain.crew.TimeLogs;
import attendance.model.repository.AttendanceRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;

public class AttendanceService {

  private static final String NON_EXISTS_CREW_MESSAGE = "해당 크루는 존재하지 않습니다.";

  private final AttendanceRepository attendanceRepository;

  public AttendanceService(AttendanceRepository attendanceRepository) {
    this.attendanceRepository = attendanceRepository;
  }

  public AttendanceInformation attendance(Crew crew, LocalDateTime attendanceTime) {
    attendanceRepository.save(crew, attendanceTime);
    TimeLog findTimeLog = attendanceRepository.findTimeLogByCrewAndTimeLog(crew,
        TimeLog.from(attendanceTime));
    return new AttendanceInformation(findTimeLog.getDateTime(),
        findTimeLog.getAttendanceStatus().getName());
  }

  public Crew findCrewByName(String crewName) {
    return attendanceRepository.findCrewByName(crewName)
        .orElseThrow(() -> new IllegalArgumentException(NON_EXISTS_CREW_MESSAGE));
  }

  public AttendanceUpdatesInformation updateAttendance(Crew crew, LocalDateTime updateTime) {
    TimeLog updateTimeLog = TimeLog.from(updateTime);

    TimeLog previousTimeLog = attendanceRepository.findTimeLogByCrewAndDate(crew, updateTime.toLocalDate());

    attendanceRepository.update(crew, previousTimeLog, updateTimeLog);

    return AttendanceUpdatesInformation.of(previousTimeLog, updateTimeLog);
  }

  public List<ManagementCrewInformation> generateManagementCrewInformation(
      CrewAttendanceComparator crewAttendanceComparator) {
    return sortedCrewAttendance(crewAttendanceComparator).stream()
        .filter(CrewAttendanceStatus::isRequiredManagement)
        .map(ManagementCrewInformation::from)
        .toList();
  }

  private List<CrewAttendanceStatus> sortedCrewAttendance(
      CrewAttendanceComparator crewAttendanceComparator) {
    return attendanceRepository.findAllCrews().stream()
        .map(crew -> CrewAttendanceStatus.of(crew, attendanceRepository.findTimeLogsByCrew(crew)))
        .sorted(crewAttendanceComparator)
        .toList();
  }

  public CrewAttendanceInformation generateCrewAttendanceInformation(String crewName) {
    Entry<Crew, TimeLogs> crewAndTimeLogs = attendanceRepository.findCrewAndTimeLogsByName(crewName)
        .orElseThrow(() -> new IllegalArgumentException(NON_EXISTS_CREW_MESSAGE));
    return crewAndTimeLogs.getValue().generateCrewAttendanceInformation(crewAndTimeLogs.getKey());
  }

}
