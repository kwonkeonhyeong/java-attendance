package attendance;

public class CrewAttendance {
    private final Crew crew;
    private final AttendanceStatus attendanceStatus;

    public CrewAttendance(Crew crew, AttendanceStatus attendanceStatus) {
        this.crew = crew;
        this.attendanceStatus = attendanceStatus;
    }

    public Crew getCrew() {
        return crew;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }
}
