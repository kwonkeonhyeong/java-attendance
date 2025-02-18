package attendance;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AttendanceBook {

    public final Map<Crew, Set<LocalDateTime>> value = new HashMap<>();

    public void attendance(final String name, final LocalDateTime time){
        Crew crew = new Crew(name);
        Set<LocalDateTime> times = value.getOrDefault(crew, new HashSet<>());
        times.add(time);
        value.put(crew, times);
    }

    public Map<Crew, Set<LocalDateTime>> getValue(){
        return Collections.unmodifiableMap(value);
    }
}
