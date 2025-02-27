package attendance.model.domain.calender;

import static org.assertj.core.api.Assertions.*;

import attendance.domain.calender.Calender;
import attendance.domain.crew.TimeLog;
import attendance.domain.crew.TimeLogs;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CalenderTest {

  @Test
  void 입력한_날이_12월_중_월요일이면_TRUE_반환() {
    assertThat(Calender.isMonday(LocalDate.of(2024, 12, 2))).isTrue();
  }

  @Test
  void 입력한_날이_12월_중_월요일이_아니면_FALSE_반환() {
    assertThat(Calender.isMonday(LocalDate.of(2024, 12, 3))).isFalse();
  }

  @Test
  void 입력한_날이_12월에_속하지_않으면_예외_발생() {
    assertThatThrownBy(() -> Calender.isMonday(LocalDate.now()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 날짜는 2024년 12월에 포함되지 않습니다");
  }

  @Test
  void 출석_기록이_존재하지_않는_날의_개수를_반환() {
    long missingCount = Calender.calculateMissingAttendanceDateCount(createTimeLogs());

    assertThat(missingCount).isEqualTo(2L);
  }


  @Test
  void 출석_기록이_존재하지_않는_Date를_리스트로_반환() {
    List<LocalDate> expected = List.of(
        LocalDate.of(2024, 12, 12),
        LocalDate.of(2024, 12, 13)
    );

    List<LocalDate> missingDate = Calender.filterMissingDate(createTimeLogs());

    assertThat(expected).containsAll(missingDate);
  }

  private TimeLogs createTimeLogs() {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 13, 1)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 6)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 4, 10, 3)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 5, 10, 31)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 6, 10, 30)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 9, 13, 5)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 10, 10, 17)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 11, 10, 34)));
    return new TimeLogs(logs);
  }

}
