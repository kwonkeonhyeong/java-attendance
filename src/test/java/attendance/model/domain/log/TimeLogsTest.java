package attendance.model.domain.log;

import static org.assertj.core.api.Assertions.assertThat;

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
  void TimeLogs에서_TimeLog를_제거() {
    List<TimeLog> logs = new ArrayList<>();
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 2, 10, 0)));
    logs.add(TimeLog.from(LocalDateTime.of(2024, 12, 3, 10, 1)));
    TimeLogs timeLogs = new TimeLogs(logs);

    timeLogs.remove(TimeLog.from(LocalDateTime.of(2024, 12, 2, 10, 0)));

    assertThat(
        timeLogs.isContain(LocalDateTime.of(2024, 12, 2, 10, 0))
    ).isTrue();
  }
}