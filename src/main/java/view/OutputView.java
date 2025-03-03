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

  public void printModifiedAttendanceResult(LocalDateTime previousDateTime,
      AttendanceStatus preciousStatus, LocalDateTime modifiedDateTime, AttendanceStatus modifiedStatus) {
    System.out.printf(
        "%n%d월 %d일 %s %02d:%02d (%s) -> %02d:%02d (%s)%n",
        previousDateTime.getMonthValue(),
        previousDateTime.getDayOfMonth(),
        previousDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
        previousDateTime.getHour(),
        previousDateTime.getMinute(),
        formatAttendanceStatus(preciousStatus),
        modifiedDateTime.getHour(),
        modifiedDateTime.getMinute(),
        formatAttendanceStatus(modifiedStatus)
    );
  }

  private String formatAttendanceStatus(AttendanceStatus status) {
    if (status.equals(AttendanceStatus.ATTENDANCE)) {
      return "출석";
    }
    if (status.equals(AttendanceStatus.LATE)) {
      return "지각";
    }
    return "결석";
  }

}
