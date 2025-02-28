package attendance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimeLogTest {

  @DisplayName("캠퍼스_운영_시간이_아닌_경우_예외_발생")
  @Test
  void operationTimeTest1() {
    List<LocalDateTime> dateTimes = List.of(
        LocalDateTime.of(2025, 2, 28, 7, 59),
        LocalDateTime.of(2025, 2, 28, 23, 1)
    );

    for (LocalDateTime dateTime : dateTimes) {
      assertThatThrownBy(() ->
          new TimeLog(dateTime)
      ).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("캠퍼스 운영 시간이 아닙니다");
    }
  }

  @DisplayName("캠퍼스_운영_시간인_경우_TimeLog_생성")
  @Test
  void operationTimeTest2() {
    List<LocalDateTime> dateTimes = List.of(
        LocalDateTime.of(2025, 2, 28, 8, 0),
        LocalDateTime.of(2025, 2, 28, 23, 0)
    );

    for (LocalDateTime dateTime : dateTimes) {
      assertThatCode(() ->
          new TimeLog(dateTime)
      ).doesNotThrowAnyException();
    }
  }
}
