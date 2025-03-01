package attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecords {

  private final List<AttendanceRecord> attendanceRecords = new ArrayList<>();

  public AttendanceRecord save(AttendanceRecord attendanceRecord) {
    validateExistsAttendanceRecord(attendanceRecord);
    attendanceRecords.add(attendanceRecord);
    return attendanceRecord;
  }

  private void validateExistsAttendanceRecord(AttendanceRecord attendanceRecord) {
    if(attendanceRecords.contains(attendanceRecord)) {
      throw new IllegalArgumentException("해당 일 출석 기록이 이미 존재합니다");
    }
  }

}
