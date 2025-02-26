package attendance.domain.crew;

import java.util.HashMap;
import java.util.Map;

public class AttendanceStatusCounts {

  private final Map<AttendanceStatus, Integer> attendanceStatusCounts;

  public AttendanceStatusCounts(Map<AttendanceStatus, Integer> attendanceStatusCounts) {
    this.attendanceStatusCounts = attendanceStatusCounts;
  }

  public static AttendanceStatusCounts of(TimeLogs timeLogs) {
    Map<AttendanceStatus, Integer> attendanceStatusCounts = new HashMap<>();
    attendanceStatusCounts.put(AttendanceStatus.ATTENDANCE, timeLogs.calculateAttendanceCount());
    attendanceStatusCounts.put(AttendanceStatus.LATE, timeLogs.calculateLateCount());
    attendanceStatusCounts.put(AttendanceStatus.ABSENCE, timeLogs.calculateAbsenceCount());
    return new AttendanceStatusCounts(attendanceStatusCounts);
  }

  public int calculatePolicyAppliedAbsenceCount() {
    int absenceCount = getAbsenceCount();
    return absenceCount + applyPolicyToLateCount();
  }

  public int applyPolicyToLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE) / 3;
  }

  public int calculateRemainingLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE) % 3;
  }

  public int getAbsenceCount() {
    return attendanceStatusCounts.get(AttendanceStatus.ABSENCE);
  }

  public int getLateCount() {
    return attendanceStatusCounts.get(AttendanceStatus.LATE);
  }

  public int getAttendanceCount() {
    return attendanceStatusCounts.get(AttendanceStatus.ATTENDANCE);
  }

}
