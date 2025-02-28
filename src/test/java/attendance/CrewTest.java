package attendance;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CrewTest {

  @DisplayName("크루_닉네임은_2_4글자_만_이용_가능")
  @ParameterizedTest
  @ValueSource(strings = {"12","123","1234"})
  void crewNicknameLengthTest1(String name) {
    assertThatCode(() -> new Crew(name)).doesNotThrowAnyException();
  }

  @DisplayName("크루_닉네임이_2_4자가_아닌_경우_예외_발생")
  @ParameterizedTest
  @ValueSource(strings = {"1","12345","123456"})
  void crewNicknameLengthTest2(String name) {
    Assertions.assertThatThrownBy(() -> new Crew(name));
  }

}
