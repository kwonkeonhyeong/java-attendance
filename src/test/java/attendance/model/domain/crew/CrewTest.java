package attendance.model.domain.crew;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CrewTest {

  @DisplayName("크루_이름이_2글자에서_4글자_사이가_아닌_경우_예외가_발생합니다크루_이름이_2글자에서_4글자_사이가_아닌_경우_예외가_발생합니다")
  @ParameterizedTest
  @ValueSource(strings = {"히","히히히히히"})
  void crewNameTest(String name) {
    Assertions.assertThatThrownBy(() -> new Crew(name))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("크루 이름은 2 ~ 4글자 사이여야 합니다");
  }
}