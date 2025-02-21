package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.CrewAttendanceLogResponse;
import attendance.dto.UpdateAttendanceResponse;
import attendance.model.domain.crew.Crew;
import attendance.model.repository.AttendanceRepository;
import attendance.model.repository.CrewAttendanceDeserializer;
import attendance.model.service.AttendanceService;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AttendanceTest {

  private AttendanceRepository attendanceRepository;
  private AttendanceService attendanceService;

  @BeforeEach
  void beforeEach() {
    final CrewAttendanceDeserializer crewAttendanceDeserializer = new CrewAttendanceDeserializer();
    final Path crewAttendanceDataPath = Path.of("src/main/resources/attendances_test.csv");
    attendanceRepository = new AttendanceRepository(crewAttendanceDeserializer,
        crewAttendanceDataPath);
    attendanceService = new AttendanceService(attendanceRepository);
  }
  // 닉네임과 등교 시간을 입력하면 출석할 수 있다.

  @Test
  void 닉네임과_등교_시간을_입력하면_출석할_수_있다() {

    Crew crew = Crew.from("이든");

    LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 14, 10, 3);

    attendanceService.attendance(crew, localDateTime);

    List<LocalDateTime> values = attendanceRepository.findByCrew(crew);

    assertThat(values).contains(localDateTime);
  }

  // 출석 기록을 확인할 수 있다.
  @Test
  void 출석_기록을_확인할_수_있다() {

    String crewName = "이든";

    Crew crew = Crew.from(crewName);

    CrewAttendanceLogResponse attendanceLog = attendanceService.getAttendanceLog(crew);

    List<AttendanceLogResponse> searchTimeLogs = attendanceLog.getTimeLogs();

    String searchCrewName = attendanceLog.getCrewName();

    assertThat(crewName).isEqualTo(searchCrewName);
    assertThat(searchTimeLogs).isNotEmpty();
  }

  // 이미 출석한 경우, 다시 출석할 수 없으며 수정 기능을 이용하도록 안내한다.
  @Test
  void 이미_출석한_경우_다시_출석할_수_없으며_수정_기능을_이용하도록_안내한다() {

    Crew crew = Crew.from("이든");

    LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 14, 10, 3);

    attendanceService.attendance(crew, localDateTime);

    assertThatIllegalStateException()
        .isThrownBy(() -> attendanceService.attendance(crew, localDateTime))
        .withMessage("금일 출석 기록이 이미 존재합니다.");
  }

  // 출석 확인을 수정하려면 닉네임, 수정하려는 날짜, 등교 시간을 입력하여 기록을 수정할 수 있다.
  @Test
  void 출석을_수정할_수_있다() {

    Crew crew = Crew.from("이든");

    LocalDateTime attendanceTime = LocalDateTime.of(2024, 12, 14, 14, 20);

    attendanceService.attendance(crew, attendanceTime);

    LocalDateTime timeToEdit = LocalDateTime.of(2024, 12, 14, 14, 20);

    attendanceService.updateAttendance(crew, timeToEdit);

    List<LocalDateTime> values = attendanceRepository.findByCrew(crew);

    assertThat(values).contains(timeToEdit);
  }

  // 수정 후에는 변경 전과 변경 후의 출석 기록을 확인할 수 있다.
  @Test
  void 수정_후에는_변경_전과_변경_후의_출석_기록을_확인할_수_있다() {

    Crew crew = Crew.from("이든");

    LocalDateTime attendanceTime = LocalDateTime.of(2024, 12, 14, 10, 20);

    attendanceService.attendance(crew, attendanceTime);

    LocalDateTime timeToEdit = LocalDateTime.of(2024, 12, 14, 10, 4);

    UpdateAttendanceResponse updateAttendanceResponse = attendanceService.updateAttendance(crew,
        timeToEdit);

    assertThat(updateAttendanceResponse.getBefore()).isEqualTo(attendanceTime);
    assertThat(updateAttendanceResponse.getAfter()).isEqualTo(timeToEdit);
  }

  /*
  // 닉네임을 입력하여 크루 출석 기록을 확인할 수 있다.
  void 닉네임을_입력하여_크루_출석_기록_확인(String crewName, List<LocalDateTime> times, int existCount,
      int nonExistCount) {

    CrewAttendanceLogResponse attendanceLog = attendanceService.getAttendanceLog(
        Crew.from(crewName));

    assertAll(
        () -> assertThat(attendanceLog.getCrewName()).isEqualTo(crewName),
        () -> assertThat(
            attendanceLog.getTimeLogs().stream().map(AttendanceLogResponse::getDate)).isSorted(),
        () -> assertThat(attendanceLog.getTimeLogs().stream().map(AttendanceLogResponse::getTime)
            .filter(Objects::isNull).count()).isEqualTo(nonExistCount),
        () -> assertThat(attendanceLog.getTimeLogs().stream().map(AttendanceLogResponse::getTime)
            .filter(Objects::nonNull).count()).isEqualTo(existCount)
    );
  }

    // 전날까지의 크루 출석 기록을 바탕으로 제적 위험자를 파악한다.
    // 제적 위험자는 제적 대상자, 면담 대상자, 경고 대상자순으로 출력하며, 대상 항목별 정렬 순서는 지각을 결석으로 간주하여 내림차순한다.
    // 출석 상태가 같으면 닉네임으로 오름차순 정렬한다.
    @ParameterizedTest
    @MethodSource("전날까지의_크루_출석_기록을_통해_제적_위험자_정렬해서_반환_테스트")
    void 전날까지의_크루_출석_기록을_통해_제적_위험자_정렬해서_반환(List<AttendanceData> attendanceData,
                                           List<SimpleImmutableEntry<String, String>> expected) {
        AttendanceRepository attendanceRepository = new AttendanceRepository(crewAttendanceDeserializer,
                crewAttendanceDataPath);
        AttendanceService service = new AttendanceService(attendanceRepository);
        attendanceData.forEach(data ->
                data.times.forEach(
                        time -> attendanceRepository.save(Crew.from(data.crewName), time)
                )
        );

        List<RequiresManagementCrewResponse> requiresManagementCrewResponses = service.getRequiresManagementCrews(
                crewAttendanceComparator);
        List<String> dangerCrewNames = requiresManagementCrewResponses.stream()
                .map(RequiresManagementCrewResponse::getCrewName).toList();

        assertThat(dangerCrewNames).containsExactlyElementsOf(expected.stream().map(Entry::getKey).toList());

        // 제적, 면담, 경고를 Status 반환해 주는 무언가 구현 (enum) 예상
        List<SimpleImmutableEntry<String, String>> actual = requiresManagementCrewResponses.stream()
                .map(value -> new SimpleImmutableEntry<>(value.getCrewName(), value.getManagementStatus()))
                .toList();

        assertThat(actual).containsExactlyElementsOf(expected);

    }

    private static class AttendanceData {
        private final String crewName;
        private final List<LocalDateTime> times;

        public AttendanceData(String crewName, List<LocalDateTime> absenceTimes, List<LocalDateTime> lateTimes) {
            this.crewName = crewName;
            this.times = new LinkedList<>(absenceTimes);
            this.times.addAll(lateTimes);
        }
    }*/
  }
