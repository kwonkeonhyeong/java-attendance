package attendance.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public enum AttendanceStatus {

    ABSENCE("결석"),
    LATE("지각"),
    ATTENDANCE("출석");

    private static final LocalTime MONDAY_LATE_TIME = LocalTime.of(13, 6);
    private static final LocalTime MONDAY_ABSENCE_TIME = LocalTime.of(13, 31);

    private static final LocalTime WEEKDAY_LATE_TIME = LocalTime.of(10, 6);
    private static final LocalTime WEEKDAY_ABSENCE_TIME = LocalTime.of(10, 31);

    private final String name;

    AttendanceStatus(String name) {
        this.name = name;
    }

    public static AttendanceStatus from(LocalDateTime dateTime) {
        if (isAbsence(dateTime)) {
            return ABSENCE;
        }
        if (isLate(dateTime)) {
            return LATE;
        }
        return ATTENDANCE;
    }

    private static boolean isAbsence(LocalDateTime dateTime) {
        if (Calender.isMonday(dateTime.toLocalDate())) {
            return isTimeAfter(dateTime.toLocalTime(), MONDAY_ABSENCE_TIME);
        }
        return isTimeAfter(dateTime.toLocalTime(), WEEKDAY_ABSENCE_TIME);
    }

    private static boolean isLate(LocalDateTime dateTime) {
        if (Calender.isMonday(dateTime.toLocalDate())) {
            return isTimeBetween(dateTime.toLocalTime(), MONDAY_LATE_TIME, MONDAY_ABSENCE_TIME.plusMinutes(1));
        }
        return isTimeBetween(dateTime.toLocalTime(), WEEKDAY_LATE_TIME, WEEKDAY_ABSENCE_TIME.plusMinutes(1));
    }

    private static boolean isTimeAfter(LocalTime time, LocalTime baseTime) {
        return time.isAfter(baseTime);
    }

    private static boolean isTimeBetween(LocalTime time, LocalTime startTime, LocalTime endTime) {
        return time.isAfter(startTime) && time.isBefore(endTime);
    }
}
