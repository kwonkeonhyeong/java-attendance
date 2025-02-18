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

        Set<LocalDateTime> times = values.getOrDefault(crew, new HashSet<>());
        times.add(time);
        values.put(crew, times);
    }

    public Map<Crew, Set<LocalDateTime>> getValues() {
        return Collections.unmodifiableMap(values);
    }
}
