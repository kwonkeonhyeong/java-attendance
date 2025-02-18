package attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class AttendanceTest {

    private static final String crewName = "pobi";
    private static final LocalDateTime attendanceTime =
            LocalDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.of(13, 20));

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

        LocalDateTime timeToModify = LocalDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.of(14, 20));

        attendanceBook.edit(crewName, timeToModify);
    }

    private AttendanceBook init() {
        AttendanceBook attendanceBook = new AttendanceBook();
        attendanceBook.attendance(crewName, attendanceTime);
        return attendanceBook;
    }
}
