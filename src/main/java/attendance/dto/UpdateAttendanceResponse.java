package attendance.dto;

import attendance.model.AttendanceStatus;
import java.time.LocalDateTime;

public class UpdateAttendanceResponse {

    private final LocalDateTime before;
    private final LocalDateTime after;

    private final String beforeStatus;
    private final String afterStatus;

    private UpdateAttendanceResponse(LocalDateTime before, LocalDateTime after, String beforeStatus,
                                     String afterStatus) {
        this.before = before;
        this.after = after;
        this.beforeStatus = beforeStatus;
        this.afterStatus = afterStatus;
    }

    public static UpdateAttendanceResponse of(LocalDateTime before, LocalDateTime after, AttendanceStatus beforeStatus,
                                              AttendanceStatus afterStatus) {
        return new UpdateAttendanceResponse(before, after, beforeStatus.getName(), afterStatus.getName());
    }

    public LocalDateTime getBefore() {
        return before;
    }

    public LocalDateTime getAfter() {
        return after;
    }

    public String getBeforeStatus() {
        return beforeStatus;
    }

    public String getAfterStatus() {
        return afterStatus;
    }
}
