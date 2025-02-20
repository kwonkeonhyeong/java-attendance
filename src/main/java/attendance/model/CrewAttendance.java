package attendance.model;

import attendance.model.domain.crew.Crew;
import java.time.LocalDateTime;
import java.util.Collection;

public class CrewAttendance {
    private final Crew crew;
    private final CrewAttendanceStatus crewAttendanceStatus;

    private CrewAttendance(Crew crew, CrewAttendanceStatus crewAttendanceStatus) {
        this.crew = crew;
        this.crewAttendanceStatus = crewAttendanceStatus;
    }

    public static CrewAttendance of(Crew crew, Collection<LocalDateTime> times) {
        return new CrewAttendance(crew, CrewAttendanceStatus.of(times));
    }

    public Crew getCrew() {
        return crew;
    }

    public CrewAttendanceStatus getAttendanceStatus() {
        return crewAttendanceStatus;
    }

    public boolean requiresManagement() {
        return crewAttendanceStatus.requiresManagement();
    }
}
