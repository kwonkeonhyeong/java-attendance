package attendance;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceAnalyzer {

    private static final LocalTime MONDAY_START_TIME = LocalTime.of(13, 0);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);

    public boolean isAbsence(LocalDateTime time) {
        if (time.getDayOfWeek() == DayOfWeek.MONDAY) {
            return isAbsence(time, MONDAY_START_TIME);
        }
        return isAbsence(time, START_TIME);
    }

    public boolean isLate(LocalDateTime time) {
        if (time.getDayOfWeek() == DayOfWeek.MONDAY) {
            return isLate(time, MONDAY_START_TIME);
        }
        return isLate(time, START_TIME);
    }

    private boolean isAbsence(LocalDateTime time, LocalTime startTime) {
        return time.toLocalTime().isAfter(startTime.plusMinutes(30));
    }

    private boolean isLate(LocalDateTime time, LocalTime startTime) {
        return time.toLocalTime().isAfter(startTime.plusMinutes(5))
                && time.toLocalTime().isBefore(startTime.plusMinutes(31));
    }

}
