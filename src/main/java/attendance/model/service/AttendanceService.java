package attendance.model.service;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.CrewAttendanceLogResponse;
import attendance.dto.RequiresManagementCrewResponse;
import attendance.dto.UpdateAttendanceResponse;
import attendance.model.domain.crew.CrewAttendanceStatus;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.comprator.CrewAttendanceComparator;
import attendance.model.domain.log.TimeLog;
import attendance.model.domain.log.TimeLogs;
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

  public AttendanceLogResponse attendance(Crew crew, LocalDateTime attendanceTime) {
    attendanceRepository.save(crew, attendanceTime);
    TimeLog findTimeLog = attendanceRepository.findTimeLogByCrewAndTimeLog(crew,
        TimeLog.from(attendanceTime));
    return new AttendanceLogResponse(findTimeLog.getDateTime(),
        findTimeLog.getAttendanceStatus().getName());
  }

  public Crew findCrewByName(String crewName) {
    return attendanceRepository.findCrewByName(crewName)
        .orElseThrow(() -> new IllegalArgumentException(NON_EXISTS_CREW_MESSAGE));
  }

  public UpdateAttendanceResponse updateAttendance(Crew crew, LocalDateTime updateTime) {
    TimeLog updateTimeLog = TimeLog.from(updateTime);

    TimeLog previousTimeLog = attendanceRepository.findTimeLogByCrewAndDate(crew, updateTime.toLocalDate());

    attendanceRepository.update(crew, previousTimeLog, updateTimeLog);

    return UpdateAttendanceResponse.of(previousTimeLog, updateTimeLog);
  }

  public List<RequiresManagementCrewResponse> getRequiresManagementCrews(
      CrewAttendanceComparator crewAttendanceComparator) {

    return getSortedCrewAttendance(crewAttendanceComparator).stream()
        .filter(CrewAttendanceStatus::requiresManagement)
        .map(RequiresManagementCrewResponse::from)
        .toList();
  }

  public CrewAttendanceLogResponse getAttendanceLog(String crewName) {
    Entry<Crew, TimeLogs> crewAndTimeLogs = attendanceRepository.findCrewAndTimeLogsByName(crewName)
        .orElseThrow(() -> new IllegalArgumentException(NON_EXISTS_CREW_MESSAGE));
    return crewAndTimeLogs.getValue().getCrewAttendanceLogResponse(crewAndTimeLogs.getKey());
  }

  private List<CrewAttendanceStatus> getSortedCrewAttendance(
      CrewAttendanceComparator crewAttendanceComparator) {
    return attendanceRepository.findAllCrews().stream()
        .map(crew -> CrewAttendanceStatus.of(crew, attendanceRepository.findTimeLogsByCrew(crew)))
        .sorted(crewAttendanceComparator)
        .toList();
  }
}
