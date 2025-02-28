package attendance;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CrewTest {
  @Test
  void 크루_이름은_2_4글자_만_이용_가능() {
    assertThatCode(() -> new Crew("1234")).doesNotThrowAnyException();
  }

  @Test
  void 크루_이름이_2_4자가_아닌_경우_예외_발생() {
    Assertions.assertThatThrownBy(() -> new Crew("12345"));
  }
}
