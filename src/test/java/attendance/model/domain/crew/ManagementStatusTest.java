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
            new LateCount(1), new AbsenceCount(0), ManagementStatus.NONE
        ),
        Arguments.arguments(
            new LateCount(5), new AbsenceCount(0), ManagementStatus.NONE
        ),
        Arguments.arguments(
            new LateCount(0), new AbsenceCount(1), ManagementStatus.NONE
        ),
        Arguments.arguments(
            new LateCount(0), new AbsenceCount(2), ManagementStatus.WARNING
        ),
        Arguments.arguments(
            new LateCount(6), new AbsenceCount(0), ManagementStatus.WARNING
        ),
        Arguments.arguments(
            new LateCount(3), new AbsenceCount(1), ManagementStatus.WARNING
        ),
        Arguments.arguments(
            new LateCount(0), new AbsenceCount(3), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            new LateCount(3), new AbsenceCount(2), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            new LateCount(9), new AbsenceCount(0), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            new LateCount(15),new  AbsenceCount(0), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            new LateCount(0), new AbsenceCount(5), ManagementStatus.COUNSELING
        ),
        Arguments.arguments(
            new LateCount(18),new  AbsenceCount(0), ManagementStatus.EXPULSION
        ),
        Arguments.arguments(
            new LateCount(0), new AbsenceCount(6), ManagementStatus.EXPULSION
        ),
        Arguments.arguments(
            new LateCount(9), new AbsenceCount(3), ManagementStatus.EXPULSION
        )
    );
  }

}
