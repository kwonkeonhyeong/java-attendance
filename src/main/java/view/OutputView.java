package view;

import attendance.AttendanceStatus;
import attendance.Crew;
import attendance.ManagementStatus;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class OutputView {

  public void printCheckedAttendanceResult(LocalDateTime dateTime, AttendanceStatus status) {
    System.out.printf(
        "%n%d월 %d일 %s %s:%s (%s)",
        dateTime.getMonthValue(),
        dateTime.getDayOfMonth(),
        dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
        formatHour(dateTime.getHour()),
        formatMinute(dateTime.getHour(), dateTime.getMinute()),
        formatAttendanceStatus(status)
    );
  }

  public void printModifiedAttendanceResult(LocalDateTime previousDateTime,
      AttendanceStatus preciousStatus, LocalDateTime modifiedDateTime,
      AttendanceStatus modifiedStatus) {
    System.out.printf(
        "%n%d월 %d일 %s %s:%s (%s) -> %s:%s (%s)",
        previousDateTime.getMonthValue(),
        previousDateTime.getDayOfMonth(),
        previousDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
        formatHour(previousDateTime.getHour()),
        formatMinute(previousDateTime.getHour(), previousDateTime.getMinute()),
        formatAttendanceStatus(preciousStatus),
        formatHour(modifiedDateTime.getHour()),
        formatMinute(modifiedDateTime.getHour(), modifiedDateTime.getMinute()),
        formatAttendanceStatus(modifiedStatus)
    );
  }

  public void printCrewAttendanceRecords(String nickname,
      LinkedHashMap<LocalDateTime, AttendanceStatus> searched) {
    System.out.printf("%n이번 달 %s의 출석 기록입니다.", nickname);
    for (Entry<LocalDateTime, AttendanceStatus> record : searched.entrySet()) {
      printCheckedAttendanceResult(record.getKey(), record.getValue());
    }
  }

  public void printCrewAttendanceResult(Map<AttendanceStatus, Integer> result,
      ManagementStatus status) {
    System.out.printf("""
            %n
            출석: %d회
            지각: %d회
            결석: %d회
            %n
            """,
        result.get(AttendanceStatus.ATTENDANCE),
        result.get(AttendanceStatus.LATE),
        result.get(AttendanceStatus.ABSENCE)
    );
    if (!status.equals(ManagementStatus.GENERAL)) {
      System.out.printf("%s 대상자입니다.%n", formatManagementStatus(status));
    }
  }

  public void printManagementCrews(Map<Crew, ManagementStatus> crewsManagementStatus,
      Map<Crew, Map<AttendanceStatus, Integer>> crewsAttendanceResult) {
    System.out.println("제적 위험자 조회 결과");
    for (Entry<Crew, ManagementStatus> managementResult : crewsManagementStatus.entrySet()) {
      Crew crew = managementResult.getKey();
      Map<AttendanceStatus, Integer> attendanceResult = crewsAttendanceResult.get(crew);
      System.out.printf("%n- %s: 결석 %d, 지각 %d회 (%s)",
          crew.getName(),
          attendanceResult.get(AttendanceStatus.ABSENCE),
          attendanceResult.get(AttendanceStatus.LATE),
          formatManagementStatus(managementResult.getValue())
      );
    }
  }

  private String formatHour(int hour) {
    if (hour == 0) {
      return "--";
    }
    return String.format("%02d", hour);
  }

  private String formatMinute(int hour, int minute) {
    if (hour == 0 && minute == 0) {
      return "--";
    }
    return String.format("%02d", minute);
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

  private String formatManagementStatus(ManagementStatus status) {
    if (status.equals(ManagementStatus.EXPULSION)) {
      return "제적";
    }
    if (status.equals(ManagementStatus.INTERVIEW)) {
      return "면담";
    }
    if (status.equals(ManagementStatus.WARNING)) {
      return "경고";
    }
    return "일반";
  }

}
