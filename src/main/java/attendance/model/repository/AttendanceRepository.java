package attendance.model.repository;

import attendance.model.domain.log.TimeLogs;
import attendance.model.domain.log.TimeLog;
import attendance.model.domain.crew.Crew;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class AttendanceRepository {

  private final Map<Crew, TimeLogs> values;

  public AttendanceRepository(CrewAttendanceDeserializer crewAttendanceDeserializer, Path path) {
    this.values = crewAttendanceDeserializer.readAll(path);
  }

  public TimeLogs findTimeLogsByCrew(Crew crew) {
    validateCrewExistence(crew);
    return values.get(crew);
  }

  public TimeLog findTimeLogByCrewAndDate(Crew crew, LocalDate date) {
    validateCrewExistence(crew);
    return values.get(crew).getLog(date);
  }

  public void save(Crew crew, LocalDateTime dateTime) {
    TimeLogs logs = values.getOrDefault(crew, new TimeLogs(new ArrayList<>()));
    logs.add(TimeLog.from(dateTime));
    values.put(crew, logs);
  }

  public boolean existsByCrew(Crew crew) {
    return values.containsKey(crew);
  }

  public void update(Crew crew, TimeLog previousTime, TimeLog updatedTime) {
    validateCrewExistence(crew);
    values.get(crew).add(updatedTime);
    deleteAttendanceByCrew(crew, previousTime);
  }

  public void deleteAttendanceByCrew(Crew crew, TimeLog dateTimeToDelete) {
    validateCrewExistence(crew);
    values.get(crew).remove(dateTimeToDelete);
  }

  public TimeLog findTimeLogByCrewAndTimeLog(Crew crew, TimeLog dateTime) {
    validateCrewExistence(crew);
    return values.get(crew).getLog(dateTime);
  }

  public List<Crew> findAllCrews() {
    return values.keySet().stream()
        .toList();
  }

  public Optional<Crew> findCrewByName(String crewName) {
    return values.keySet().stream()
        .filter(crew -> crew.getName().equals(crewName))
        .findFirst();
  }

  public Optional<Entry<Crew, TimeLogs>> findCrewAndTimeLogsByName(String crewName) {
    return values.entrySet().stream()
        .filter(entry ->
            entry.getKey().getName().equals(crewName)
        )
        .findFirst();
  }

  private void validateCrewExistence(Crew crew) {
    if (!existsByCrew(crew)) {
      throw new IllegalArgumentException("존재하지 않는 크루입니다.");
    }
  }

}
