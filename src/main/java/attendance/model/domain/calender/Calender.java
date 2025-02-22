package attendance.model.domain.calender;

import attendance.model.domain.log.TimeLog;
import attendance.model.domain.log.TimeLogs;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public enum Calender {

  MONDAY(List.of(2, 9, 16, 23, 30)),
  WEEKDAY(List.of(2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 16, 17, 18, 19, 20, 23, 24, 26, 27, 30, 31)),
  WEEKEND(List.of(1, 7, 8, 14, 15, 21, 22, 28, 29)),
  HOLIDAY(List.of(25));

  private static final int YEAR = 2024;
  private static final Month MONTH = Month.DECEMBER;
  private final List<Integer> days;

  Calender(List<Integer> days) {
    this.days = days;
  }

  public static TimeLogs getNotExistsDatesBeforeToday(TimeLogs dates) {
    List<TimeLog> nonExistsTimeLogsBeforeToday = getDatesBefore(
        LocalDate.of(2024, 12, 14)).stream()
        .filter(date -> !dates.isContain(date))
        .map(date -> TimeLog.of(date, null))
        .toList();

    return new TimeLogs(nonExistsTimeLogsBeforeToday);
  }

  private static List<LocalDate> getDatesBefore(LocalDate date) {
    return WEEKDAY.getDays().stream()
        .filter(result -> result.isBefore(date))
        .toList();
  }

  public static boolean isMonday(LocalDate date) {
    if (date.getYear() != YEAR && date.getMonth() != MONTH) {
      throw new IllegalArgumentException("해당 날짜는 2024년 12월에 포함되지 않습니다.");
    }
    return MONDAY.days.contains(date.getDayOfMonth());
  }

  public List<LocalDate> getDays() {
    return days.stream()
        .map(day -> LocalDate.of(YEAR, MONTH, day))
        .toList();
  }

}
