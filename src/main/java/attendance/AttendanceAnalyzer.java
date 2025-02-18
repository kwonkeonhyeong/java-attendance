package attendance;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class AttendanceAnalyzer {

    private static final LocalTime MONDAY_START_TIME = LocalTime.of(13, 0);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    // Set<LocalDateTime> 받아서 결석, 지각 횟수 응답.
    // 결석, 지각 횟수, 위험 상태를 관리하는 도메인 구현

    public AttendanceStatus analyzeAttendance(Set<LocalDateTime> times) {
        int absenceCount = (int) times.stream().filter(time -> {
            if (time.getDayOfWeek() == DayOfWeek.MONDAY) {
                return isAbsence(time, MONDAY_START_TIME);
            }
            return isAbsence(time, START_TIME);
        }).count();

        int lateCount = (int) times.stream().filter(time -> {
            if (time.getDayOfWeek() == DayOfWeek.MONDAY) {
                return isLate(time, MONDAY_START_TIME);
            }
            return isLate(time, START_TIME);
        }).count();

        return AttendanceStatus.of(absenceCount, lateCount);
    }

    private boolean isAbsence(LocalDateTime time, LocalTime startTime) {
        return time.toLocalTime().isAfter(startTime.plusMinutes(30));
    }

    private boolean isLate(LocalDateTime time, LocalTime startTime) {
        return time.toLocalTime().isAfter(startTime.plusMinutes(5));
    }

}
