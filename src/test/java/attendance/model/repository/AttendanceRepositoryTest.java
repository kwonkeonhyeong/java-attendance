package attendance.model.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import attendance.model.domain.crew.Crew;
import attendance.model.domain.log.TimeLog;
import attendance.model.domain.log.TimeLogs;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AttendanceRepositoryTest {

  private AttendanceRepository attendanceRepository;

  @BeforeEach
  void beforeEach() {
    final CrewAttendanceDeserializer crewAttendanceDeserializer = new CrewAttendanceDeserializer();
    final Path crewAttendanceDataPath = Path.of("src/main/resources/attendances_test.csv");
    attendanceRepository = new AttendanceRepository(crewAttendanceDeserializer,
        crewAttendanceDataPath);
  }

  @Test
  void 크루로_크루의_TimeLogs_찾기 () {
    TimeLogs timeLogs = attendanceRepository.findTimeLogsByCrew(new Crew("이든"));
    assertThat(timeLogs).isNotNull();
  }

  @Test
  void 크루_정보를_이용해서_크루_TimeLog를_찾을_때_크루가_존재하지_않으면_예외_발생 () {
    assertThatThrownBy(() -> attendanceRepository.findTimeLogsByCrew(new Crew("히포")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("존재하지 않는 크루입니다");
  }

  @Test
  void 크루와_날짜로_크루의_TimeLog_찾기 () {
    Crew crew = new Crew("이든");
    LocalDate date = LocalDate.of(2024, 12, 2);
    TimeLog timeLog = attendanceRepository.findTimeLogByCrewAndDate(crew, date);
    assertThat(timeLog.getDateTime().toLocalDate()).isEqualTo(date);
  }

  @Test
  void 크루와_날짜_정보를_저장합니다() {
    Crew crew = new Crew("이든");
    LocalDateTime dateTime = LocalDateTime.of(2025,2,23,10,5);
    attendanceRepository.save(crew, dateTime);

    TimeLog timeLog = attendanceRepository.findTimeLogByCrewAndDate(crew,
        dateTime.toLocalDate());

    assertThat(timeLog.getDateTime()).isEqualTo(dateTime);
  }

  @Test
  void 존재하는_크루인지_확인() {
    Crew crew = new Crew("이든");
    assertThat(attendanceRepository.existsByCrew(crew)).isTrue();
  }

  @Test
  void 크루의_TimeLog를_수정() {
    Crew crew = new Crew("이든");
    LocalDate date = LocalDate.of(2024, 12, 2);
    LocalDateTime updateDateTime = LocalDateTime.of(2024, 12, 2, 10, 38);
    TimeLog previousTimeLog = attendanceRepository.findTimeLogByCrewAndDate(crew, date);
    TimeLog updateTimeLog = TimeLog.from(LocalDateTime.of(2024, 12, 2, 10, 38));
    attendanceRepository.update(crew,previousTimeLog,updateTimeLog);

    TimeLog updatedTimeLog = attendanceRepository.findTimeLogByCrewAndDate(crew, date);

    assertThat(updatedTimeLog.getDateTime()).isEqualTo(updateDateTime);
  }

  @Test
  void 크루의_TimeLog를_삭제() {
    Crew crew = new Crew("이든");
    LocalDateTime dateTime = LocalDateTime.of(2025,2,23,10,5);
    attendanceRepository.save(crew, dateTime);

    TimeLog timeLog = attendanceRepository.findTimeLogByCrewAndDate(crew,
        dateTime.toLocalDate());

    assertThat(timeLog.getDateTime()).isEqualTo(dateTime);

    attendanceRepository.deleteAttendanceByCrew(crew, timeLog);

    assertThatThrownBy(() -> attendanceRepository.findTimeLogByCrewAndTimeLog(crew, timeLog))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 크루는 해당 일시의 출석 기록이 없습니다");
  }

  @Test
  void 크루와_TimeLog로_TimeLog_찾기() {
    Crew crew = new Crew("이든");
    LocalDateTime dateTime = LocalDateTime.of(2024,12,2,13,2);
    TimeLog log = TimeLog.from(dateTime);
    TimeLog timeLog = attendanceRepository.findTimeLogByCrewAndTimeLog(crew, log);
    assertThat(log).isEqualTo(timeLog);
  }

  @Test
  void 크루_이름으로_크루_찾기() {
    Crew crew = attendanceRepository.findCrewByName("이든").orElse(null);
    assertThat(crew).isNotNull();
  }

  @Test
  void 크루_이름으로_크루와_timeLogs_찾기() {
    Entry<Crew, TimeLogs> attendanceLogs = attendanceRepository.findCrewAndTimeLogsByName("이든").orElse(null);
    assertThat(attendanceLogs).isNotNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"이든","빙봉","짱수","빙티","쿠키"})
  void 전체_크루_가져오기(String name) {
    Crew crew = new Crew(name);
    List<Crew> allCrews = attendanceRepository.findAllCrews();

    assertThat(allCrews.contains(crew)).isTrue();
  }
}