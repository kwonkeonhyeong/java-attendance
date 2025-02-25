package attendance.model.domain.crew;

import attendance.model.domain.Information.AttendanceInformation;
import attendance.model.domain.Information.CrewAttendanceInformation;
import attendance.model.domain.calender.Calender;
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

  public void addAll(TimeLogs timeLogs) {
    logs.addAll(timeLogs.getLogs());
  }

  public void remove(TimeLog log) {
    logs.remove(log);
  }

  public List<TimeLog> getLogs() {
    return logs.stream().toList();
  }

  public TimeLog getLog(TimeLog log) {
    return logs.stream()
        .filter(value -> value.equals(log))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(NON_EXISTS_TIME_LOG));
  }

  public TimeLog getLog(LocalDate date) {
    return logs.stream()
        .filter(value -> value.isSame(date))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(NON_EXISTS_TIME_LOG));
  }

  public boolean isContain(LocalDate date) {
    return logs.stream()
        .anyMatch(log -> log.isSame(date));
  }

  public boolean isContain(LocalDateTime dateTime) {
    return logs.stream()
        .anyMatch(log -> log.isSame(dateTime.toLocalDate()));
  }

  public CrewAttendanceInformation getCrewAttendanceInformation(Crew crew) {
    List<AttendanceInformation> attendanceInformation = Stream.concat(
            createAttendanceInformation().stream(), createMissingAttendanceInformation().stream()
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

  public int calculateAbsenceCount() {
    int presentAbsenceCount = Math.toIntExact(
        logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.ABSENCE)
            .count()
    );
    int missingAttendanceCount = Calender.calculateMissingAttendanceDateCount(this);
    return presentAbsenceCount + missingAttendanceCount;
  }

  public int calculateLateCount() {
    return Math.toIntExact(
        logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.LATE)
            .count()
    );
  }

  public int calculateAttendanceCount() {
    return Math.toIntExact(
        logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.ATTENDANCE)
            .count()
    );
  }

  private List<AttendanceInformation> createAttendanceInformation() {
    return logs.stream()
        .map(TimeLog::getAttendanceInformation)
        .toList();
  }

  private List<AttendanceInformation> createMissingAttendanceInformation() {

    return Calender.filterMissingDate(this).stream()
        .map(date -> new AttendanceInformation(date, null, AttendanceStatus.ABSENCE.getName()))
        .toList();
  }

  private void validateConflict(TimeLog timeLog) {
    logs.stream()
        .filter(value -> value.equals(timeLog))
        .findFirst()
        .ifPresent(value -> {
          throw new IllegalArgumentException(DUPLICATE_TIME_LOG);
        });
  }

  @Override
  public String toString() {
    return "TimeLogs{" +
        "logs=" + logs +
        '}';
  }
}
