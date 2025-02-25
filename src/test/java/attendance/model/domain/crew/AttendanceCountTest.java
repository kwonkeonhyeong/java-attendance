package attendance.model.domain.crew;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AttendanceCountTest {

  @Test
  void 출석_횟수는_음수가_될_수_없습니다() {
    Assertions.assertThatThrownBy(() -> {
          new AttendanceCount(-1);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("출석 횟수는 음수가 될 수 없습니다");
  }

}
