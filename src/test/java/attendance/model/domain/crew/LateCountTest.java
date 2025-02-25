package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LateCountTest {

  @Test
  void 지각_횟수는_음수가_될_수_없습니다() {
    Assertions.assertThatThrownBy(() -> {
      LateCount.from(-1);
    }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("지각 횟수는 음수가 될 수 없습니다");
  }

  @Test
  void 지각_3회_시_결석_1회로_변환한_계산_값_반환() {
    LateCount lateCount = LateCount.from(6);
    assertThat(lateCount.calculatePolicyAppliedAbsenceCount()).isEqualTo(2);
  }

  @Test
  void 지각_3회_시_결석_1회로_반환_후_남은_지각_횟수_반환() {
    LateCount lateCount = LateCount.from(7);
    assertThat(lateCount.calculatePolicyAppliedLateCount()).isEqualTo(1);
  }

}
