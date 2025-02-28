package attendance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HolidayTest {

  @DisplayName("입력한_날이_Holiday에_포함_되는_경우_true")
  @Test
  void containsHolidayReturnTrueTest() {
    LocalDate date = LocalDate.of(2025,12,25);
    assertThat(Holiday.contains(date)).isTrue();
  }

  @DisplayName("입력한_날이_Holiday에_포함_되는_경우_false")
  @Test
  void containsHolidayReturnFalseTest() {
    LocalDate date = LocalDate.of(2025, 2, 28);
    assertThat(Holiday.contains(date)).isFalse();
  }
}
