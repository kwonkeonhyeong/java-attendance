package attendance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AttendanceRecordTest {

  @DisplayName("크루_닉네임과_출석_시간을_통해_AttendanceRecord_내에서_Crew_TimeLog_생성")
  @Test
  void attendanceRecordConstructorTest() {
    AttendanceRecord attendanceRecord = new AttendanceRecord("히포", LocalDateTime.of(2025, 2, 28, 10, 0));

    assertAll(
        () -> assertThat(attendanceRecord.getNickname()).isInstanceOf(Crew.class),
        () -> assertThat(attendanceRecord.getTimeLog()).isInstanceOf(TimeLog.class)
    );
  }

}
