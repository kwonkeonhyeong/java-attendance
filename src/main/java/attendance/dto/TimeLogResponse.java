package attendance.dto;

import static attendance.model.AttendanceStatus.ABSENCE;

import attendance.model.AttendanceAnalyzer;
import attendance.model.AttendanceStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeLogResponse {

    private final LocalDate date;
    private final LocalTime time;
    private final AttendanceStatus attendanceStatus;

    private TimeLogResponse(LocalDateTime dateTime, AttendanceStatus attendanceStatus) {
        this.date = dateTime.toLocalDate();
        this.time = dateTime.toLocalTime();
        this.attendanceStatus = attendanceStatus;
    }

    private TimeLogResponse(LocalDate date, AttendanceStatus attendanceStatus) {
        this.date = date;
        this.time = null;
        this.attendanceStatus = attendanceStatus;
    }

    public static TimeLogResponse of(LocalDateTime dateTime, AttendanceAnalyzer attendanceAnalyzer) {
        return new TimeLogResponse(dateTime, AttendanceStatus.of(dateTime, attendanceAnalyzer));
    }

    public static TimeLogResponse of(LocalDate date) {
        return new TimeLogResponse(date, ABSENCE);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }
}
