package attendance;

import static org.assertj.core.api.Assertions.assertThat;

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
        AttendanceBook attendanceBook = new AttendanceBook();
        attendanceBook.attendance(crewName, attendanceTime);

        Set<LocalDateTime> value = attendanceBook.getValue().get(new Crew(crewName));
        assertThat(value).contains(attendanceTime);
    }
}
