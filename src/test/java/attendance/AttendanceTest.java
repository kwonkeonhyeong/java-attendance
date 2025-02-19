package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AttendanceTest {

    private static final String crewName = "pobi";
    private static final LocalDateTime attendanceTime =
            LocalDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.of(13, 20));

    static Stream<Arguments> 닉네임을_통해_전날까지의_크루_출석_기록_확인_테스트_케이스() {
        return Stream.of(
                Arguments.of("pobi", List.of(
                        LocalDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.of(13, 20)),
                        LocalDateTime.of(LocalDate.of(2024, 12, 15), LocalTime.of(15, 20)),
                        LocalDateTime.of(LocalDate.of(2024, 12, 13), LocalTime.of(15, 20)))
                )
        );
    }

    static Stream<Arguments> 전날까지의_크루_출석_기록을_통해_제적_위험자_정렬해서_반환_테스트() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new AttendanceData("pobi", getAbsenceTimes(0, 1), (getLateTimes(1, 5))),
                                new AttendanceData("anni", getAbsenceTimes(0, 2), (getLateTimes(2, 4)))
                        ),
                        List.of(new SimpleImmutableEntry<>("anni", "경고"), new SimpleImmutableEntry<>("pobi", "경고"))
                ),
                Arguments.of(
                        List.of(
                                new AttendanceData("abd", getAbsenceTimes(0, 4), (getLateTimes(4, 5))),
                                new AttendanceData("abc", getAbsenceTimes(0, 4), (getLateTimes(4, 5)))
                        ),
                        List.of(new SimpleImmutableEntry<>("abc", "면담"), new SimpleImmutableEntry<>("abd", "면담"))
                ),
                Arguments.of(
                        List.of(
                                new AttendanceData("pobi", getAbsenceTimes(0, 5), (getLateTimes(5, 8))),
                                new AttendanceData("anni", getAbsenceTimes(0, 6), (getLateTimes(6, 6)))
                        ),
                        List.of(new SimpleImmutableEntry<>("anni", "제적"), new SimpleImmutableEntry<>("pobi", "제적"))
                )
        );
    }

    static List<LocalDateTime> getAbsenceTimes(int start, int end) {
        return Calender.WEEKDAY.getDays().subList(start, end).stream()
                .map(day -> LocalDateTime.of(day, LocalTime.of(10, 32)))
                .toList();
    }

    static List<LocalDateTime> getLateTimes(int start, int end) {
        return Calender.WEEKDAY.getDays().subList(start, end).stream()
                .map(day -> LocalDateTime.of(day, LocalTime.of(10, 7)))
                .toList();
    }

    // 닉네임과 등교 시간을 입력하면 출석할 수 있다.
    @Test
    void 닉네임과_등교_시간을_입력하면_출석할_수_있다() {
        AttendanceBook attendanceBook = init();

        Set<LocalDateTime> values = attendanceBook.getValues().get(new Crew(crewName));
        assertThat(values).contains(attendanceTime);
    }

    // 출석 후 출석 기록을 확인할 수 있다.
    @Test
    void 출석_후_출석_기록을_확인할_수_있다() {
        AttendanceBook attendanceBook = init();

        Set<LocalDateTime> values = attendanceBook.getValues().get(new Crew(crewName));
        assertThat(values).contains(attendanceTime);
    }

    // 이미 출석한 경우, 다시 출석할 수 없으며 수정 기능을 이용하도록 안내한다.
    @Test
    void 이미_출석한_경우_다시_출석할_수_없으며_수정_기능을_이용하도록_안내한다() {
        AttendanceBook attendanceBook = init();

        assertThatIllegalStateException()
                .isThrownBy(() -> attendanceBook.attendance(crewName, attendanceTime))
                .withMessage("금일 출석 기록이 이미 존재합니다.");
    }

    // 출석 확인을 수정하려면 닉네임, 수정하려는 날짜, 등교 시간을 입력하여 기록을 수정할 수 있다.
    @Test
    void 출석을_수정할_수_있다() {
        AttendanceBook attendanceBook = init();

        LocalDateTime timeToEdit = LocalDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.of(14, 20));

        attendanceBook.edit(crewName, timeToEdit);

        Set<LocalDateTime> values = attendanceBook.getValues().get(new Crew(crewName));
        assertThat(values).contains(timeToEdit);
    }

    // 수정 후에는 변경 전과 변경 후의 출석 기록을 확인할 수 있다.
    @Test
    void 수정_후에는_변경_전과_변경_후의_출석_기록을_확인할_수_있다() {
        AttendanceBook attendanceBook = init();

        LocalDateTime timeToEdit = LocalDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.of(14, 20));

        AttendanceEditResponse attendanceEditResponse = attendanceBook.edit(crewName, timeToEdit);

        assertThat(attendanceEditResponse.getBefore()).isEqualTo(attendanceTime);
        assertThat(attendanceEditResponse.getAfter()).isEqualTo(timeToEdit);
    }

    // 닉네임을 입력하면 전날까지의 크루 출석 기록을 확인할 수 있다.
    @ParameterizedTest
    @MethodSource("닉네임을_통해_전날까지의_크루_출석_기록_확인_테스트_케이스")
    void 닉네임을_통해_전날까지의_크루_출석_기록_확인(String crewName, List<LocalDateTime> times) {
        AttendanceBook attendanceBook = new AttendanceBook();
        times.forEach(time -> attendanceBook.attendance(crewName, time));

        AttendanceLogResponse attendanceLog = attendanceBook.getLog(crewName);

        assertAll(
                () -> assertThat(attendanceLog.getCrewName()).isEqualTo(crewName),
                () -> assertThat(attendanceLog.getTimeLogs()).isSorted()
        );
    }

    // 전날까지의 크루 출석 기록을 바탕으로 제적 위험자를 파악한다.
    // 제적 위험자는 제적 대상자, 면담 대상자, 경고 대상자순으로 출력하며, 대상 항목별 정렬 순서는 지각을 결석으로 간주하여 내림차순한다.
    // 출석 상태가 같으면 닉네임으로 오름차순 정렬한다.
    @ParameterizedTest
    @MethodSource("전날까지의_크루_출석_기록을_통해_제적_위험자_정렬해서_반환_테스트")
    void 전날까지의_크루_출석_기록을_통해_제적_위험자_정렬해서_반환(List<AttendanceData> attendanceData,
                                           List<SimpleImmutableEntry<String, String>> expected) {
        AttendanceBook attendanceBook = new AttendanceBook();
        attendanceData.forEach(data ->
                data.times.forEach(
                        time -> attendanceBook.attendance(data.crewName, time)
                )
        );

        List<DangerCrewResponse> dangerCrewResponses = attendanceBook.getDangerCrews(new DangerCrewSorter());
        List<String> dangerCrewNames = dangerCrewResponses.stream().map(DangerCrewResponse::getName).toList();

        assertThat(dangerCrewNames).containsExactlyElementsOf(expected.stream().map(Entry::getKey).toList());

        // 제적, 면담, 경고를 Status 반환해 주는 무언가 구현 (enum) 예상
        List<SimpleImmutableEntry<String, String>> actual = dangerCrewResponses.stream()
                .map(value -> new SimpleImmutableEntry<>(value.getName(), value.getDangerStatus()))
                .toList();

        assertThat(actual).containsExactlyElementsOf(expected);

    }

    private AttendanceBook init() {
        AttendanceBook attendanceBook = new AttendanceBook();
        attendanceBook.attendance(crewName, attendanceTime);
        return attendanceBook;
    }

    private static class AttendanceData {
        private final String crewName;
        private final List<LocalDateTime> times;

        public AttendanceData(String crewName, List<LocalDateTime> absenceTimes, List<LocalDateTime> lateTimes) {
            this.crewName = crewName;
            this.times = new LinkedList<>(absenceTimes);
            this.times.addAll(lateTimes);
        }
    }
}
