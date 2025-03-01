package attendance;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AttendanceBookTest {
  @DisplayName("출석_확인_시_크루가_존재하지_않는_경우_예외_발생")
  @Test
  void notExistsCrewTest() {
    AttendanceBook attendanceBook = new AttendanceBook();
    Assertions.assertThatThrownBy(() ->
      attendanceBook.checkAttendance("히포", LocalDateTime.of(2025,2,28,10,0))
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록되지 않은 크루입니다");
  }
}
