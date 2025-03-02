package attendance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRecords {

  private final List<AttendanceRecord> attendanceRecords = new ArrayList<>();

  public AttendanceRecord save(AttendanceRecord attendanceRecord) {
    validateExistentAttendanceRecord(attendanceRecord);
    attendanceRecords.add(attendanceRecord);
    return attendanceRecord;
  }

  private void validateExistentAttendanceRecord(AttendanceRecord attendanceRecord) {
    if(attendanceRecords.contains(attendanceRecord)) {
      throw new IllegalArgumentException("해당 일 출석 기록이 이미 존재합니다");
    }
  }

  public AttendanceRecord modifyAttendanceRecord(AttendanceRecord updateAttendanceRecord) {
    AttendanceRecord previousRecord = attendanceRecords.stream()
        .filter(record -> record.equals(updateAttendanceRecord))
        .findFirst()
            .orElse(new AttendanceRecord(updateAttendanceRecord.getDateTime()));

    attendanceRecords.remove(previousRecord);
    attendanceRecords.add(updateAttendanceRecord);
    return previousRecord;
  }
}
