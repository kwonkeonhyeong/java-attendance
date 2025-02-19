package attendance.dto;

import java.time.LocalDateTime;

public class AttendanceEditResponse {

    private final LocalDateTime before;
    private final LocalDateTime after;

    public AttendanceEditResponse(LocalDateTime before, LocalDateTime after) {
        this.before = before;
        this.after = after;
    }

    public LocalDateTime getBefore() {
        return before;
    }

    public LocalDateTime getAfter() {
        return after;
    }
}
