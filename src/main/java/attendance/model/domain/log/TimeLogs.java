package attendance.model.domain.log;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.CrewAttendanceLogResponse;
import attendance.model.domain.crew.AbsenceCount;
import attendance.model.domain.crew.AttendanceCount;
import attendance.model.domain.crew.AttendanceStatus;
import attendance.model.domain.calender.Calender;
import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.CrewAttendanceStatus;
import attendance.model.domain.crew.LateCount;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class TimeLogs {

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
        .orElseThrow(() -> new IllegalArgumentException("해당 크루는 해당 일시의 출석 기록이 없습니다."));
  }

  public TimeLog getLog(LocalDate date) {
    return logs.stream()
        .filter(value -> value.isSame(date))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("해당 크루는 해당 일시의 출석 기록이 없습니다."));
  }

  public boolean isContain(LocalDate date) {
    return logs.stream()
        .anyMatch(log -> log.isSame(date));
  }

  public boolean isContain(LocalDateTime dateTime) {
    return logs.stream()
        .anyMatch(log -> log.isSame(dateTime));
  }

  public CrewAttendanceLogResponse getCrewAttendanceLogResponse(Crew crew) {
    TimeLogs notExistsAttendanceTimeLogs = Calender.getNotExistsDatesBeforeToday(this);
    List<AttendanceLogResponse> existsAttendanceLogResponses = this.getAttendanceLogResponses();
    List<AttendanceLogResponse> notExistsAttendanceLogResponses = notExistsAttendanceTimeLogs.getAttendanceLogResponses();
    List<AttendanceLogResponse> attendanceLogResponses = Stream.concat(
            existsAttendanceLogResponses.stream(), notExistsAttendanceLogResponses.stream()
        )
        .sorted(Comparator.comparing(AttendanceLogResponse::getDate))
        .toList();
    return CrewAttendanceLogResponse.of(
        crew,
        calculateAttendanceCount(),
        calculateLateCount(),
        calculateAbsenceCount(),
        attendanceLogResponses,
        CrewAttendanceStatus.of(crew, this).getManagementStatus()
    );
  }

  public AbsenceCount calculateAbsenceCount() {
    int value = Math.toIntExact(
        logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.ABSENCE)
            .count()
    );
    return AbsenceCount.from(value);
  }

  public LateCount calculateLateCount() {
    int value = Math.toIntExact(
        logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.LATE)
            .count()
    );
    return LateCount.from(value);
  }

  public AttendanceCount calculateAttendanceCount() {
    int value = Math.toIntExact(
        logs.stream()
            .filter(log -> log.getAttendanceStatus() == AttendanceStatus.LATE)
            .count()
    );
    return AttendanceCount.from(value);
  }

  private List<AttendanceLogResponse> getAttendanceLogResponses() {
    return logs.stream()
        .map(TimeLog::getAttendanceLogResponse)
        .toList();
  }

  private void validateConflict(TimeLog timeLog) {
    logs.stream()
        .filter(value -> value.equals(timeLog))
        .findFirst()
        .ifPresent(value -> {
          throw new IllegalStateException("금일 출석 기록이 이미 존재합니다.");
        });
  }

  @Override
  public String toString() {
    return "TimeLogs{" +
        "logs=" + logs +
        '}';
  }
}
