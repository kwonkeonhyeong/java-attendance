package attendance.model.domain.calender;

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
  private static final String NOT_CONTAIN_DECEMBER = "해당 날짜는 2024년 12월에 포함되지 않습니다";

  private final List<Integer> days;

  Calender(List<Integer> days) {
    this.days = days;
  }

  public static int countMissingAttendanceDays(TimeLogs timeLogs) {
    return (int) WEEKDAY.getDays().stream()
        .filter(date -> date.isBefore(LocalDate.of(2024, 12, 14)))
        .filter(date -> !timeLogs.isContain(date))
        .count();
  }

  public static List<LocalDate> getMissingDate(TimeLogs timeLogs) {
    return WEEKDAY.getDays().stream()
        .filter(date -> date.isBefore(LocalDate.of(2024, 12, 14)))
        .filter(date -> !timeLogs.isContain(date))
        .toList();
  }

  public static boolean isMonday(LocalDate date) {
    if (date.getYear() != YEAR && date.getMonth() != MONTH) {
      throw new IllegalArgumentException(NOT_CONTAIN_DECEMBER);
    }
    return MONDAY.days.contains(date.getDayOfMonth());
  }

  public List<LocalDate> getDays() {
    return days.stream()
        .map(day -> LocalDate.of(YEAR, MONTH, day))
        .toList();
  }

  public boolean isContainDate(LocalDate date) {
    return days.contains(date.getDayOfMonth());
  }

}
