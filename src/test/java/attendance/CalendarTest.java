package attendance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalendarTest {
  @DisplayName("입력_날짜_이전까지의_캠퍼스_이용_가능_날짜_반환")
  @Test
  void returnAvailableDatesTest() {
    List<LocalDate> availableDates = List.of(
        LocalDate.of(2025, 2, 3),
        LocalDate.of(2025, 2, 4),
        LocalDate.of(2025, 2, 5),
        LocalDate.of(2025, 2, 6),
        LocalDate.of(2025, 2, 7),
        LocalDate.of(2025, 2, 10)
    );

    LocalDate searchDate = LocalDate.of(2025,2,11);
    List<LocalDate> dates = Calendar.from(searchDate).getAvailableDates(searchDate);

    for (LocalDate availableDate : availableDates) {
      assertThat(dates.contains(availableDate)).isTrue();
    }
    assertThat(dates.contains(LocalDate.of(2025,2,11))).isFalse();
  }

  @DisplayName("입력_날짜_이전까지의_캠퍼스_이용_가능_날짜_반환_후_이용_불가능한_날짜_포함_확인")
  @Test
  void checkUnavailableDatesTest() {
    List<LocalDate> unavailableDates = List.of(
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 2, 2),
        LocalDate.of(2025, 2, 8),
        LocalDate.of(2025, 2, 9)
    );

    LocalDate searchDate = LocalDate.of(2025,2,11);
    List<LocalDate> dates = Calendar.from(searchDate).getAvailableDates(searchDate);

    for (LocalDate unavailableDate : unavailableDates) {
      assertThat(dates.contains(unavailableDate)).isFalse();
    }
  }
}
