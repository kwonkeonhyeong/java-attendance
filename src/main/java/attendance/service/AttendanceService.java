package attendance.service;

import attendance.domain.Information.AttendanceInformation;
import attendance.domain.Information.CrewAttendanceInformation;
import attendance.domain.Information.ManagementCrewInformation;
import attendance.domain.Information.AttendanceUpdatesInformation;
import attendance.domain.crew.CrewAttendanceStatus;
import attendance.domain.crew.Crew;
import attendance.domain.crew.comprator.CrewAttendanceComparator;
import attendance.domain.crew.TimeLog;
import attendance.domain.crew.TimeLogs;
import attendance.repository.AttendanceRepository;
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
