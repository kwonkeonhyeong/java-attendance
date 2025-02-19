package attendance.model.repository;

import attendance.dto.AttendanceEditResponse;
import attendance.dto.AttendanceLogResponse;
import attendance.dto.DangerCrewResponse;
import attendance.model.AttendanceAnalyzer;
import attendance.model.CrewAttendance;
import attendance.model.DangerCrewSorter;
import attendance.model.crew.Crew;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AttendanceRepository {

    public final Map<Crew, List<LocalDateTime>> values = new HashMap<>();

    public List<LocalDateTime> findByCrew(Crew crew) {
        return values.get(crew);
    }

    public void save(Crew crew, LocalDateTime dateTime) {
        if (existsByCrew(crew)) {
            validateConflict(crew, dateTime);
        }
        List<LocalDateTime> times = values.getOrDefault(crew, new ArrayList<>());
        times.add(dateTime);
        values.put(crew, times);
    }

    public boolean existsByCrew(Crew crew) {
        return values.containsKey(crew);
    }

    public AttendanceEditResponse update(Crew crew, LocalDateTime dateTimeToUpdate) {
        LocalDateTime before = findDateTimeByCrewAndDate(crew, dateTimeToUpdate.toLocalDate())
                .orElseThrow(() -> new IllegalArgumentException("해당 크루는 해당 일자의 출석 기록이 없습니다."));
        values.get(crew).add(dateTimeToUpdate);
        deleteAttendanceByCrew(crew, before);
        return new AttendanceEditResponse(before, dateTimeToUpdate);
    }

    public void deleteAttendanceByCrew(Crew crew, LocalDateTime dateTimeToDelete) {
        values.get(crew).remove(dateTimeToDelete);
    }

    private Optional<LocalDateTime> findDateTimeByCrewAndDate(Crew crew, LocalDate date) {
        return values.get(crew).stream()
                .filter(time -> time.toLocalDate().equals(date))
                .findFirst();
    }

    public AttendanceLogResponse getLog(final String name) {
        AttendanceAnalyzer attendanceAnalyzer = new AttendanceAnalyzer();
        Crew crew = new Crew(name);
        List<LocalDateTime> timeLogs = values.get(crew).stream().toList();

        return AttendanceLogResponse.of(crew, timeLogs, attendanceAnalyzer);
    }

    public List<DangerCrewResponse> getDangerCrews(DangerCrewSorter dangerCrewSorter) {
        AttendanceAnalyzer attendanceAnalyzer = new AttendanceAnalyzer();

        return getSortedCrewStatusByAttendance(attendanceAnalyzer, dangerCrewSorter).stream()
                .map(DangerCrewResponse::from)
                .toList();
    }

    private List<CrewAttendance> getSortedCrewStatusByAttendance(AttendanceAnalyzer attendanceAnalyzer,
                                                                 DangerCrewSorter dangerCrewSorter) {

        return values.entrySet().stream()
                .map(value -> CrewAttendance.of(value.getKey(), value.getValue(), attendanceAnalyzer))
                .filter(CrewAttendance::isDanger)
                .sorted(dangerCrewSorter::compare)
                .toList();
    }

    private void validateConflict(Crew crew, LocalDateTime dateTime) {

        values.get(crew).stream()
                .filter(value -> value.toLocalDate().equals(dateTime.toLocalDate()))
                .findFirst()
                .ifPresent(value -> {
                    throw new IllegalStateException("금일 출석 기록이 이미 존재합니다.");
                });
    }

}
