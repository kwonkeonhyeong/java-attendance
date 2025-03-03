package attendance;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ManagementStatusTest {
  @DisplayName("결석_횟수를_기준으로_크루_관리_상태_반환")
  @ParameterizedTest
  @MethodSource("createManagementStatus")
  void returnManagementStatusTest(int absenceCont ,ManagementStatus status) {
    Assertions.assertThat(ManagementStatus.from(absenceCont)).isEqualTo(status);
  }

  static Stream<Arguments> createManagementStatus() {
    return Stream.of(
        Arguments.arguments(6, ManagementStatus.EXPLUSION),
        Arguments.arguments(5, ManagementStatus.INTERVIEW),
        Arguments.arguments(3, ManagementStatus.INTERVIEW),
        Arguments.arguments(2, ManagementStatus.WARNING),
        Arguments.arguments(1, ManagementStatus.GENERAL),
        Arguments.arguments(0, ManagementStatus.GENERAL)
    );
  }
}
