package attendance;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public enum Holiday {

  DECEMBER(Month.DECEMBER, List.of(25));

  private final Month monthOfHoliday;
  private final List<Integer> holidays;

  Holiday(Month monthOfHoliday, List<Integer> holidays) {
    this.monthOfHoliday = monthOfHoliday;
    this.holidays = holidays;
  }

  public static boolean contains(LocalDate date) {
    return Arrays.stream(Holiday.values())
        .anyMatch(month ->
            month.monthOfHoliday.equals(date.getMonth()) &&
                month.holidays.contains(date.getDayOfMonth()));
  }
}
