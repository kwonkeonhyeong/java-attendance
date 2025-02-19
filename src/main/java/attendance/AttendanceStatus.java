package attendance;

import java.time.LocalDateTime;

public enum AttendanceStatus {

    ABSENCE("결석"),
    LATE("지각"),
    ATTENDANCE("출석");

    private final String name;

    AttendanceStatus(String name) {
        this.name = name;
    }

    public static AttendanceStatus of(LocalDateTime time, AttendanceAnalyzer attendanceAnalyzer) {
        if (attendanceAnalyzer.isAbsence(time)) {
            return ABSENCE;
        }
        if (attendanceAnalyzer.isLate(time)) {
            return LATE;
        }
        return ATTENDANCE;
    }
}
