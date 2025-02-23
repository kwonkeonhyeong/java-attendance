package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ManagementStatusTest {

  @ParameterizedTest
  @MethodSource("createAttendanceResultCountForManagementStatus")
  void 출석_및_지각_count_를_기준으로_크루_관리_상태를_확인(LateCount lateCount, AbsenceCount absenceCount, ManagementStatus status) {
    ManagementStatus managementStatus = ManagementStatus.of(absenceCount, lateCount);
    assertThat(managementStatus).isEqualTo(status);
  }

  private static Stream<Arguments> createAttendanceResultCountForManagementStatus() {
    return Stream.of(
        Arguments.arguments(
            LateCount.from(1), AbsenceCount.from(0), ManagementStatus.NONE
        ),
        Arguments.arguments(
            LateCount.from(5), AbsenceCount.from(0), ManagementStatus.NONE
        ),
        Arguments.arguments(
            LateCount.from(0), AbsenceCount.from(1), ManagementStatus.NONE
        ),
        Arguments.arguments(
            LateCount.from(0), AbsenceCount.from(2), ManagementStatus.WARNING
        ),
        Arguments.arguments(
            LateCount.from(6), AbsenceCount.from(0), ManagementStatus.WARNING
        ),
        Arguments.arguments(
            LateCount.from(3), AbsenceCount.from(1), ManagementStatus.WARNING
        ),
        Arguments.arguments(
            LateCount.from(0), AbsenceCount.from(3), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            LateCount.from(3), AbsenceCount.from(2), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            LateCount.from(9), AbsenceCount.from(0), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            LateCount.from(15), AbsenceCount.from(0), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            LateCount.from(0), AbsenceCount.from(5), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            LateCount.from(18), AbsenceCount.from(0), ManagementStatus.EXPULSION
        ),
        Arguments.arguments(
            LateCount.from(0), AbsenceCount.from(6), ManagementStatus.EXPULSION
        ),
        Arguments.arguments(
            LateCount.from(9), AbsenceCount.from(3), ManagementStatus.EXPULSION
        )
    );
  }

}