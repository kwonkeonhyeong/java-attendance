package attendance.model.domain.calender;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CalenderTest {
  @Test
  void 입력한_날이_12월_중_월요일이면_TRUE_반환() {
    assertThat(Calender.isMonday(LocalDate.of(2024,12,2))).isTrue();
  }

  @Test
  void 입력한_날이_12월_중_월요일이_아니면_FALSE_반환() {
    assertThat(Calender.isMonday(LocalDate.of(2024,12,3))).isFalse();
  }

  @Test
  void 입력한_날이_12월에_속하지_않으면_예외_발생() {
    assertThatThrownBy(() -> Calender.isMonday(LocalDate.now()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 날짜는 2024년 12월에 포함되지 않습니다");

  }

}