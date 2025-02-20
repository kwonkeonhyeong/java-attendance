package attendance.dto;

import attendance.model.AttendanceStatus;

public class AttendanceStatusResponse {

    private final String attendanceStatus;

    private AttendanceStatusResponse(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public static AttendanceStatusResponse from(AttendanceStatus attendanceStatus) {
        return new AttendanceStatusResponse(attendanceStatus.getName());
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }
}
