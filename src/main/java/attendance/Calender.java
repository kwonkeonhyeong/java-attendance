package attendance;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public enum Calender {
    WEEKDAY(List.of(2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 16, 17, 18, 19, 20, 23, 24, 26, 27, 30, 31)),
    WEEKEND(List.of(1, 7, 8, 14, 15, 21, 22, 28, 29)),
    HOLIDAY(List.of(25));

    private static final int YEAR = 2024;
    private static final Month MONTH = Month.DECEMBER;
    private final List<Integer> days;

    Calender(List<Integer> days) {
        this.days = days;
    }

    public List<LocalDate> getDays() {
        return days.stream()
                .map(day -> LocalDate.of(YEAR, MONTH, day))
                .toList();
    }
}
