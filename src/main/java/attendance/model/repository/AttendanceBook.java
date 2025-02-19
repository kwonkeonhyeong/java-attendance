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

public class AttendanceBook {

    public final Map<Crew, List<LocalDateTime>> values = new HashMap<>();

    public void attendance(final String name, final LocalDateTime time) {
        Crew crew = new Crew(name);
        if (values.containsKey(crew)) {
            validateAttendance(crew, time);
        }
        List<LocalDateTime> times = values.getOrDefault(crew, new ArrayList<>());
        times.add(time);
        values.put(crew, times);
    }

    public List<LocalDateTime> findByCrew(Crew crew) {
        return values.get(crew);
    }

    public AttendanceEditResponse edit(final String name, final LocalDateTime timeToEdit) {
        LocalDateTime before = values.get(new Crew(name)).stream()
                .filter(time -> time.toLocalDate().equals(timeToEdit.toLocalDate())).findFirst().get();
        values.get(new Crew(name)).add(timeToEdit);
        values.get(new Crew(name)).remove(before);

        return new AttendanceEditResponse(before, timeToEdit);
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

    private void validateAttendance(Crew crew, LocalDateTime time) {
        LocalDate covertTime = time.toLocalDate();
        boolean isAlreadyAttendance = values.get(crew).stream().anyMatch(value ->
                value.toLocalDate().equals(covertTime)
        );
        if (isAlreadyAttendance) {
            throw new IllegalStateException("금일 출석 기록이 이미 존재합니다.");
        }
    }

}
