package attendance.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public enum AttendanceStatus {
    
    ATTENDANCE("출석"),
    LATE("지각"),
    ABSENCE("결석");

    private static final LocalTime MONDAY_LATE_TIME = LocalTime.of(13, 5);
    private static final LocalTime MONDAY_ABSENCE_TIME = LocalTime.of(13, 30);

    private static final LocalTime WEEKDAY_LATE_TIME = LocalTime.of(10, 5);
    private static final LocalTime WEEKDAY_ABSENCE_TIME = LocalTime.of(10, 30);

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

    public static AttendanceStatus from(String name) {
        return Arrays.stream(values())
                .filter(attendanceStatus -> attendanceStatus.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 출석 상태가 없습니다."));
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

    public String getName() {
        return name;
    }
}
