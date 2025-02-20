package attendance.dto;

import attendance.model.AttendanceStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceLogResponse {

    private final LocalDate date;
    private final LocalTime time;
    private final AttendanceStatusResponse attendanceStatusResponse;

    private AttendanceLogResponse(LocalDateTime dateTime, AttendanceStatusResponse attendanceStatusResponse) {
        this.date = dateTime.toLocalDate();
        this.time = dateTime.toLocalTime();
        this.attendanceStatusResponse = attendanceStatusResponse;
    }

    private AttendanceLogResponse(LocalDate date, AttendanceStatusResponse attendanceStatusResponse) {
        this.date = date;
        this.time = null;
        this.attendanceStatusResponse = attendanceStatusResponse;
    }

    public static AttendanceLogResponse of(LocalDateTime dateTime, AttendanceStatus attendanceStatus) {
        return new AttendanceLogResponse(dateTime, AttendanceStatusResponse.from(attendanceStatus));
    }

    public static AttendanceLogResponse of(LocalDate date, AttendanceStatus attendanceStatus) {
        return new AttendanceLogResponse(date, AttendanceStatusResponse.from(attendanceStatus));
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public AttendanceStatusResponse getAttendanceStatusResponse() {
        return attendanceStatusResponse;
    }
}
