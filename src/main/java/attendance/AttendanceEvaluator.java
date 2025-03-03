package attendance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceEvaluator {

  public Map<AttendanceStatus, Integer> calculateAttendanceResult(
      List<AttendanceRecord> searchedAttendanceRecords) {
    Map<AttendanceStatus, Integer> calculateResult = new HashMap<>();
    calculateResult.put(AttendanceStatus.ATTENDANCE, 0);
    calculateResult.put(AttendanceStatus.LATE, 0);
    calculateResult.put(AttendanceStatus.ABSENCE, 0);
    for (AttendanceRecord attendanceRecord: searchedAttendanceRecords) {
      AttendanceStatus status = AttendanceStatus.from(attendanceRecord);
      calculateResult.put(status, calculateResult.get(status) + 1);
    }
    return calculateResult;
  }

}
