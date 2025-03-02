package attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecords {

  private final List<AttendanceRecord> attendanceRecords = new ArrayList<>();

  public ExistentAttendanceRecord save(ExistentAttendanceRecord existentAttendanceRecord) {
    validateExistentAttendanceRecord(existentAttendanceRecord);
    attendanceRecords.add(existentAttendanceRecord);
    return existentAttendanceRecord;
  }

  private void validateExistentAttendanceRecord(ExistentAttendanceRecord existentAttendanceRecord) {
    if (attendanceRecords.contains(existentAttendanceRecord)) {
      throw new IllegalArgumentException("해당 일 출석 기록이 이미 존재합니다");
    }
  }

  public AttendanceRecord modifyAttendanceRecord(ExistentAttendanceRecord updateAttendanceRecord) {
    AttendanceRecord previousRecord = attendanceRecords.stream()
        .filter(record -> record.equals(updateAttendanceRecord))
        .findFirst()
        .orElse(new AbsenceRecord(updateAttendanceRecord.getDateTime().toLocalDate()));
    if (previousRecord.isExists()) {
      attendanceRecords.remove(previousRecord);
    }
    attendanceRecords.add(updateAttendanceRecord);
    return previousRecord;
  }
}
