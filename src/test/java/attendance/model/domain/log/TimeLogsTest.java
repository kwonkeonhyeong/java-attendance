package attendance.model.domain.log;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeLogsTest {

  // TimeLogs, TimeLog
  @Test
  void TimeLog와_날짜가_같으면_true를_반환() {
    TimeLog log = TimeLog.from(LocalDateTime.of(2024, 12, 13, 13, 0));

    Assertions.assertThat(log.isSame(LocalDate.of(2024,12,13))).isTrue();
  }

  @Test
  void TimeLog와_날짜가_같으면_false를_반환 () {
    TimeLog log = TimeLog.from(LocalDateTime.of(2024, 12, 13, 13, 0));

    Assertions.assertThat(log.isSame(LocalDate.of(2024,12,13))).isTrue();
  }

  @Test
  void TimeLog에_출석시간이_존재하지_않는_경우_DateTime_반환_시_예외_발생() {
    TimeLog log = TimeLog.of(LocalDate.of(2024,12,13), null);

    Assertions.assertThatThrownBy(log::getDateTime)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("출석 시간이 존재하지 않습니다");
  }

}