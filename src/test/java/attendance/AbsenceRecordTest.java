package attendance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AbsenceRecordTest {

  @DisplayName("주말_결석_기록_생성_시_예외_발생")
  @Test
  void weekendAbsenceRecordTest() {
    List<LocalDate> dates = List.of(
        LocalDate.of(2025, 3, 1),
        LocalDate.of(2025, 3, 2)
    );
    for (LocalDate date : dates) {
      assertThatThrownBy(() ->
          new AbsenceRecord(date)
      ).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.");
    }
  }

  @DisplayName("공휴일_결석_기록_생성_시_예외_발생")
  @Test
  void holidayAbsenceRecordTest() {
    assertThatThrownBy(() ->
        new AbsenceRecord(LocalDate.of(2025, 12, 25))
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주말 또는 공휴일에는 운영하지 않습니다.");
  }

  @DisplayName("결석_기록_시간_수정_시_출석_기록으로_변경")
  @Test
  void modifyAbsenceRecord() {
    AbsenceRecord absenceRecord = new AbsenceRecord(LocalDate.of(2025, 2, 28));
    assertThat(absenceRecord.modifyTime(LocalTime.of(10, 5)))
        .isInstanceOf(ExistentAttendanceRecord.class);
  }

  @DisplayName("결석_기록_시간_수정_시_캠퍼스_운영시간_외의_시간으로_변경하면_예외_발생")
  @Test
  void outOfCampusOperatingTimeExceptionTest() {
    AbsenceRecord absenceRecord = new AbsenceRecord(LocalDate.of(2025, 2, 28));
    assertThatThrownBy(() ->
        absenceRecord.modifyTime(LocalTime.of(7, 59))
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("캠퍼스 운영 시간이 아닙니다");
  }
}
