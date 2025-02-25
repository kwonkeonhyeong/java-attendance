package attendance.model.domain.crew;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AbsenceCountTest {
  @Test
  void 결석_횟수는_음수가_될_수_없습니다() {
    Assertions.assertThatThrownBy(() -> {
          new AbsenceCount(-1);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("결석 횟수는 음수가 될 수 없습니다");
  }

  @Test
  void 지각_3회는_결석_1회라는_정책을_적용한_결석_횟수_반환() {
    LateCount lateCount = new LateCount(6);
    AbsenceCount absenceCount = new AbsenceCount(2);
    Assertions.assertThat(absenceCount.getPolicyAppliedAbsenceCount(lateCount)).isEqualTo(4);
  }
}
