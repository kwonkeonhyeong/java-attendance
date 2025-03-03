package attendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRecords {

  private final List<AttendanceRecord> attendanceRecords;

  public AttendanceRecords() {
    this.attendanceRecords = new ArrayList<>();
  }

  public AttendanceRecords(List<AttendanceRecord> attendanceRecords) {
    this.attendanceRecords = attendanceRecords;
  }

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

  public List<AttendanceRecord> searchRecords(LocalDate searchDate) {
    List<AttendanceRecord> searchedAttendanceRecords = new ArrayList<>();
    for (LocalDate availableDate : filterAvailableDates(searchDate)) {
      searchedAttendanceRecords.add(findAttendanceRecordBy(availableDate));
    }
    return searchedAttendanceRecords;
  }

  private AttendanceRecord findAttendanceRecordBy(LocalDate searchDate) {
    return filterSameYearAndMonthRecords(searchDate).stream()
        .filter(record -> record.isSameDate(searchDate))
        .findFirst()
        .orElse(new AbsenceRecord(searchDate));
  }

  private List<LocalDate> filterAvailableDates(LocalDate searchDate) {
    Calendar searchMonth = Calendar.from(searchDate);
    return searchMonth.getAvailableDates(searchDate);
  }

  private List<AttendanceRecord> filterSameYearAndMonthRecords(LocalDate searchDate) {
    return attendanceRecords.stream()
        .filter(record -> record.isSameYearAndMonth(searchDate))
        .toList();
  }

}
