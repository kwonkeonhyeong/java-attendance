package attendance.domain.crew;

import attendance.domain.Information.AttendanceInformation;
import attendance.domain.Information.AttendanceUpdatesInformation;
import attendance.domain.Information.CrewAttendanceInformation;
import attendance.domain.calender.Calender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class TimeLogs {

  private static final String NON_EXISTS_TIME_LOG = "해당 크루는 해당 일시의 출석 기록이 없습니다";
  private static final String DUPLICATE_TIME_LOG = "금일 출석 기록이 이미 존재합니다";

  private final List<TimeLog> logs;

  public TimeLogs(List<TimeLog> logs) {
    this.logs = logs;
  }

  public void add(TimeLog log) {
    validateConflict(log);
    logs.add(log);
  }

  private void validateConflict(TimeLog attendTimeLog) {
    logs.stream()
        .filter(timeLog -> timeLog.isSame(attendTimeLog))
        .findFirst()
        .ifPresent(timeLog -> {
          throw new IllegalArgumentException(DUPLICATE_TIME_LOG);
        });
  }

  public void addAll(TimeLogs timeLogs) {
    logs.addAll(timeLogs.getLogs());
  }

  public List<TimeLog> getLogs() {
    return logs.stream().toList();
  }

  public CrewAttendanceInformation generateCrewAttendanceInformation(Crew crew) {
    List<AttendanceInformation> attendanceInformation = Stream.concat(
            generateAttendanceInformation().stream(), generateMissingAttendanceInformation().stream()
        )
        .sorted(Comparator.comparing(AttendanceInformation::getDate))
        .toList();
    return CrewAttendanceInformation.of(
        crew,
        calculateAttendanceCount(),
        calculateLateCount(),
        calculateAbsenceCount(),
        attendanceInformation,
        CrewAttendanceStatus.of(crew, this).getManagementStatus()
    );
  }

  private List<AttendanceInformation> generateMissingAttendanceInformation() {
    return Calender.filterMissingDate(this).stream()
        .map(date -> new AttendanceInformation(date, null, AttendanceStatus.ABSENCE.getName()))
        .toList();
  }

  private List<AttendanceInformation> generateAttendanceInformation() {
    return logs.stream()
        .map(TimeLog::generateAttendanceInformation)
        .toList();
  }

  public long calculateAbsenceCount() {
    long presentAbsenceCount = logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.ABSENCE)
            .count();
    long missingAttendanceCount = Calender.calculateMissingAttendanceDateCount(this);
    return presentAbsenceCount + missingAttendanceCount;
  }

  public long calculateLateCount() {
    return logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.LATE)
            .count();
  }

  public long calculateAttendanceCount() {
    return logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.ATTENDANCE)
            .count();
  }
  
  public AttendanceUpdatesInformation modify(TimeLog updateTimeLog) {
    TimeLog previousTimeLog = logs.stream()
        .filter(timeLog -> timeLog.isSame(updateTimeLog))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(NON_EXISTS_TIME_LOG));
    logs.remove(previousTimeLog);
    logs.add(updateTimeLog);
    return AttendanceUpdatesInformation.of(previousTimeLog, updateTimeLog);
  }

  public boolean isContain(LocalDate date) {
    return logs.stream()
        .anyMatch(log -> log.isSame(date));
  }

  public boolean isContain(LocalDateTime dateTime) {
    return logs.stream()
        .anyMatch(log -> log.isSame(dateTime.toLocalDate()));
  }

  @Override
  public String toString() {
    return "TimeLogs{" +
        "logs=" + logs +
        '}';
  }

}
