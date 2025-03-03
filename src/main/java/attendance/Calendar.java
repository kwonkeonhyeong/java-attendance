package attendance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Calendar {

  JANUARY(Month.JANUARY,31),
  FEBRUARY(Month.FEBRUARY,28),
  MARCH(Month.MARCH,31),
  APRIL(Month.APRIL,30),
  MAY(Month.MAY,31),
  JUNE(Month.JUNE,30),
  JULY(Month.JULY,31),
  AUGUST(Month.AUGUST,31),
  SEPTEMBER(Month.SEPTEMBER,30),
  OCTOBER(Month.OCTOBER,31),
  NOVEMBER(Month.NOVEMBER,30),
  DECEMBER(Month.DECEMBER,31);

  private final Month thisMonth;
  private final int endDay;

  Calendar(Month thisMonth, int endDay) {
    this.thisMonth = thisMonth;
    this.endDay = endDay;
  }

  public static Calendar from(LocalDate date) {
    return Arrays.stream(Calendar.values())
        .filter(month -> month.thisMonth.equals(date.getMonth()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("입력한 날에 해당하는 달이 존재하지 않습니다"));
  }

  public List<LocalDate> getAvailableDates(LocalDate searchDate) {
    List<LocalDate> availableDates = new ArrayList<>();
    for (int date = 1; date < searchDate.getDayOfMonth(); date++) {
      LocalDate availableDate = LocalDate.of(searchDate.getYear(), thisMonth, date);
      addAvailableDate(availableDate, availableDates);
    }
    return availableDates;
  }

  private void addAvailableDate(LocalDate availableDate, List<LocalDate> availableDates) {
    if (isOperateDate(availableDate)) {
      availableDates.add(availableDate);
    }
  }

  private boolean isOperateDate(LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
      return false;
    }
    return !Holiday.contains(date);
  }

}
