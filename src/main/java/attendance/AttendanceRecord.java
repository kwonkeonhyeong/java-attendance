package attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AttendanceRecord {

  boolean isExists();

  LocalDateTime getRecord();

  boolean isSameYearAndMonth(LocalDate searchDate);

  boolean isSameDate(LocalDate searchDate);
}
