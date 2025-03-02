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

  public AttendanceRecord modifyAttendanceRecord(
      ExistentAttendanceRecord updateExistentAttendanceRecord) {
    AttendanceRecord previousRecord = attendanceRecords.stream()
        .filter(record -> record.equals(updateExistentAttendanceRecord))
        .findFirst()
        .orElse(new AbsenceRecord(updateExistentAttendanceRecord.getDateTime().toLocalDate()));
    if (previousRecord.isExists()) {
      attendanceRecords.remove(previousRecord);
    }
    attendanceRecords.add(updateExistentAttendanceRecord);
    return previousRecord;
  }
}
