package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AttendanceStatusTest {

  @Test
  void 입력한_출석_상태가_존재하지_않는_경우_예외_발생() {
    assertThatThrownBy(() -> AttendanceStatus.from("없는경우"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당하는 출석 상태가 없습니다");
  }

  @ParameterizedTest
  @MethodSource("createStatusNameExpectedData")
  void 입력한_출석_상태가_존재하는_경우_출석_상태_반환(String name, AttendanceStatus status) {
    assertThat(AttendanceStatus.from(name)).isEqualTo(status);
  }

  private static Stream<Arguments> createStatusNameExpectedData() {
    return Stream.of(
        Arguments.arguments("출석", AttendanceStatus.ATTENDANCE),
        Arguments.arguments("지각", AttendanceStatus.LATE),
        Arguments.arguments("결석", AttendanceStatus.ABSENCE)
    );
  }

}
