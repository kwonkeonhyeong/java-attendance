package view;

import attendance.AttendanceStatus;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class OutputView {

  public void printCheckedAttendanceResult(LocalDateTime dateTime, AttendanceStatus status) {
    System.out.printf(
        "%n%d월 %d일 %s %02d:%02d (%s)%n",
        dateTime.getMonthValue(),
        dateTime.getDayOfMonth(),
        dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
        dateTime.getHour(),
        dateTime.getMinute(),
        formatAttendanceStatus(status)
    );
  }

  private String formatAttendanceStatus(AttendanceStatus status) {
    if(status.equals(AttendanceStatus.ATTENDANCE)) {
      return "출석";
    }
    if(status.equals(AttendanceStatus.LATE)) {
      return "지각";
    }
    return "결석";
  }

}
