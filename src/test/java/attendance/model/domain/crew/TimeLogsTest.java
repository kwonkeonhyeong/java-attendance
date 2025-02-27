package attendance.model.domain.crew;

import static org.assertj.core.api.Assertions.assertThat;

import attendance.domain.crew.TimeLog;
import attendance.domain.crew.TimeLogs;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TimeLogsTest {

  @Test
  void TimeLog_추가_시_같은_날_동일한_기록이_존재하는_경우_예외_발생() {
    TimeLogs timeLogs = new TimeLogs(new LinkedList<>());
    TimeLog timeLog = TimeLog.from(LocalDateTime.of(2024, 12, 2, 10, 0));
    timeLogs.add(timeLog);

    TimeLog anotherTimeLog = TimeLog.from(LocalDateTime.of(2024, 12, 2, 10, 0));

    Assertions.assertThatThrownBy(() -> timeLogs.add(anotherTimeLog))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("금일 출석 기록이 이미 존재합니다");
  }

  @Test
  void TimeLogs에_다른_TimeLogs_내의_모든_출석_기록을_등록() {

    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 10, 0)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 1)));
    TimeLogs timeLogs = new TimeLogs(logs);

    List<TimeLog> anotherLogs = new ArrayList<>();
    anotherLogs.add(TimeLog.from(LocalDateTime.of(2024, 12, 5, 10, 3)));
    anotherLogs.add(TimeLog.from(LocalDateTime.of(2024, 12, 4, 10, 2)));
    TimeLogs anotherTimeLogs = new TimeLogs(anotherLogs);

    timeLogs.addAll(anotherTimeLogs);

    assertThat(
        timeLogs.isContain(LocalDateTime.of(2024, 12, 4, 10, 2))
    ).isTrue();

    assertThat(
        timeLogs.isContain(LocalDateTime.of(2024, 12, 5, 10, 3))
    ).isTrue();
  }

  @Test
  void TimeLogs에_특정_일의_TimeLog가_존재하는지_확인() {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 10, 0)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 1)));
    TimeLogs timeLogs = new TimeLogs(logs);

    assertThat(
        timeLogs.isContain(LocalDate.of(2024, 12, 2))
    ).isTrue();
  }

  @Test
  void TimeLogs_기록을_기준으로_출석_count_계산() {
    TimeLogs timeLogs = createTimeLogsForCountTest();
    assertThat(timeLogs.calculateAttendanceCount()).isEqualTo(3);
  }

  @Test
  void TimeLogs_기록을_기준으로_지각_count_계산() {
    TimeLogs timeLogs = createTimeLogsForCountTest();
    assertThat(timeLogs.calculateLateCount()).isEqualTo(3);
  }

  // 13일까지의 출석 기록이 있다고 가정합니다.
  @Test
  void TimeLogs_기록을_기준으로_결석_count_계산() {
    TimeLogs timeLogs = createTimeLogsForCountTest();
    assertThat(timeLogs.calculateAbsenceCount()).isEqualTo(4);
  }

  private TimeLogs createTimeLogsForCountTest() {
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
