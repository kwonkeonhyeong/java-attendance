package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import attendance.domain.crew.ManagementStatus;
import org.junit.jupiter.api.Test;

class ManagementStatusTest {

  @Test
  void 출석_및_지각_count_를_기준으로_크루_관리_상태를_확인() {

    ManagementStatus expulsionManagementStatus = ManagementStatus.of(6);
    ManagementStatus counselingManagementStatus = ManagementStatus.of(3);
    ManagementStatus warningManagementStatus = ManagementStatus.of(2);
    ManagementStatus noneManagementStatus = ManagementStatus.of(0);

    assertAll(
        () -> assertThat(expulsionManagementStatus).isEqualTo(ManagementStatus.EXPULSION),
        () -> assertThat(counselingManagementStatus).isEqualTo(ManagementStatus.COUNSELING),
        () -> assertThat(warningManagementStatus).isEqualTo(ManagementStatus.WARNING),
        () -> assertThat(noneManagementStatus).isEqualTo(ManagementStatus.NONE)
    );
  }

}
