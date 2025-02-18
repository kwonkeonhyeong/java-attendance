package attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AttendanceBook {

    public final Map<Crew, Set<LocalDateTime>> values = new HashMap<>();

    public void attendance(final String name, final LocalDateTime time) {
        Crew crew = new Crew(name);
        if (values.containsKey(crew)) {
            validateAttendance(crew, time);
        }
        Set<LocalDateTime> times = values.getOrDefault(crew, new HashSet<>());
        times.add(time);
        values.put(crew, times);
    }

    public Map<Crew, Set<LocalDateTime>> getValues() {
        return Collections.unmodifiableMap(values);
    }

    public AttendanceEditResponse edit(final String name, final LocalDateTime timeToEdit) {
        values.get(new Crew(name)).add(timeToEdit);

        return new AttendanceEditResponse();
    }

    public Set<LocalDateTime> getLog(final String name) {
        return values.get(new Crew(name));
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
