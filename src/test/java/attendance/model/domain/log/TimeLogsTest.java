package attendance.model.domain.log;

import java.time.LocalDateTime;
import java.util.LinkedList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TimeLogsTest {
  @Test
  void TimeLog_추가_시_같은_날_동일한_기록이_존재하는_경우_예외_발생() {
    TimeLogs timeLogs = new TimeLogs(new LinkedList<>());
    TimeLog timeLog = TimeLog.from(LocalDateTime.of(2024,12,2,10,0));
    timeLogs.add(timeLog);

    TimeLog anotherTimeLog = TimeLog.from(LocalDateTime.of(2024,12,2,10,0));

    Assertions.assertThatThrownBy(() -> timeLogs.add(anotherTimeLog))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("금일 출석 기록이 이미 존재합니다");
  }
}