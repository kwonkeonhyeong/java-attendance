package attendance.model.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import attendance.domain.AttendanceManager;
import attendance.domain.Information.AttendanceInformation;
import attendance.domain.Information.CrewAttendanceInformation;
import attendance.domain.Information.ManagementCrewInformation;
import attendance.domain.Information.AttendanceUpdatesInformation;
import attendance.domain.crew.Crew;
import attendance.domain.crew.TimeLog;
import attendance.domain.AttendanceBook;
import attendance.view.input.InputView;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AttendanceBookTest {

  private final InputView inputView = new InputView();
  private final AttendanceBook attendanceBook = new AttendanceBook(inputView.loadCrewAttendanceTimeLogs());

  // 닉네임과 등교 시간을 입력하면 출석할 수 있다.
  @Test
  void 닉네임과_등교_시간을_입력하면_출석할_수_있다() {

    Crew crew = new Crew("이든");

    LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 16, 10, 3);

    TimeLog timeLog = TimeLog.from(localDateTime);

    assertThatCode(() -> attendanceBook.attend(crew, timeLog))
        .doesNotThrowAnyException();
  }

  // 출석 기록을 확인할 수 있다.
  @Test
  void 출석_기록을_확인할_수_있다() {

    Crew crew = new Crew("이든");

    CrewAttendanceInformation crewAttendanceInformation = attendanceBook.checkAttendanceTimeLogs(crew);

    List<AttendanceInformation> attendanceInformation = crewAttendanceInformation.getAttendanceInformation();

    String searchCrewName = crewAttendanceInformation.getCrewName();

    assertThat(crew.getName()).isEqualTo(searchCrewName);
    assertThat(attendanceInformation).isNotEmpty();
  }

  // 이미 출석한 경우, 다시 출석할 수 없으며 수정 기능을 이용하도록 안내한다.
  @Test
  void 이미_출석한_경우_다시_출석할_수_없으며_수정_기능을_이용하도록_안내한다() {
    Crew crew = new Crew("이든");
    LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 10, 10, 3);
    TimeLog timeLog = TimeLog.from(localDateTime);

    assertThatThrownBy(() -> attendanceBook.attend(crew, timeLog))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("금일 출석 기록이 이미 존재합니다");
  }

  // 출석 확인을 수정하려면 닉네임, 수정하려는 날짜, 등교 시간을 입력하여 기록을 수정할 수 있다.
  @Test
  void 출석을_수정할_수_있다() {
    Crew crew = new Crew("이든");
    LocalDateTime attendanceTime = LocalDateTime.of(2024, 12, 16, 13, 30);
    TimeLog attendanceTimeLog = TimeLog.from(attendanceTime);
    attendanceBook.attend(crew, attendanceTimeLog);

    LocalDateTime modifyTime = LocalDateTime.of(2024, 12, 16, 13, 3);
    TimeLog modifyTimeLog = TimeLog.from(modifyTime);

    AttendanceUpdatesInformation attendanceUpdatesInformation = attendanceBook.modifiedAttendanceTimeLog(
        crew, modifyTimeLog);

    assertAll(
        () -> assertThat(attendanceUpdatesInformation.getBefore()).isEqualTo(attendanceTime),
        () -> assertThat(attendanceUpdatesInformation.getAfter()).isEqualTo(modifyTime),
        () -> assertThat(attendanceUpdatesInformation.getBeforeStatus()).isEqualTo("지각"),
        () -> assertThat(attendanceUpdatesInformation.getAfterStatus()).isEqualTo("출석")
    );
  }

  // 닉네임을 입력하여 크루 출석 기록을 확인할 수 있다.
  @ParameterizedTest
  @MethodSource("getCrewAttendanceLogExpectedValue")
  void 닉네임을_입력하여_크루_출석_기록_확인(String name, int attendanceCount, int lateCount, int absenceCount,
      String managementStatus) {

    CrewAttendanceInformation attendanceLog = attendanceBook.checkAttendanceTimeLogs(new Crew(name));

    assertAll(
        () -> assertThat(attendanceLog.getCrewName()).isEqualTo(name),
        () -> assertThat(attendanceLog.getAttendanceCount()).isEqualTo(attendanceCount),
        () -> assertThat(attendanceLog.getLateCount()).isEqualTo(lateCount),
        () -> assertThat(attendanceLog.getAbsenceCount()).isEqualTo(absenceCount),
        () -> assertThat(attendanceLog.getManagementStatus()).isEqualTo(managementStatus),
        () -> assertThat(
            attendanceLog.getAttendanceInformation().stream().map(AttendanceInformation::getDate)).isSorted()
    );
  }

  static Stream<Arguments> getCrewAttendanceLogExpectedValue() {
    return Stream.of(
        Arguments.arguments(
            "빙티", 3, 4, 3, "면담"
        ),
        Arguments.arguments(
            "이든", 3, 5, 2, "면담"
        ),
        Arguments.arguments(
            "빙봉", 3, 6, 1, "면담"
        ),
        Arguments.arguments(
            "쿠키", 5, 3, 2, "면담"
        ),
        Arguments.arguments(
            "짱수", 8, 0, 2, "경고"
        )
    );
  }

  @ParameterizedTest
  @MethodSource("getManagementCrewExpectedValue")
  void 전날까지의_크루_출석_기록을_통해_제적_위험자_반환(String name, int lateCount, int absenceCount,
      String managementStatus) {

    List<ManagementCrewInformation> managementCrewInformation = attendanceBook.checkManagementCrews();

    for (ManagementCrewInformation information : managementCrewInformation) {
      if (information.getCrewName().equals(name)) {
        assertAll(
            () -> assertThat(information.getCrewName()).isEqualTo(name),
            () -> assertThat(information.getLateCount()).isEqualTo(lateCount),
            () -> assertThat(information.getAbsenceCount()).isEqualTo(
                absenceCount),
            () -> assertThat(information.getManagementStatus()).isEqualTo(
                managementStatus)
        );
      }
    }
  }

  static Stream<Arguments> getManagementCrewExpectedValue() {
    return Stream.of(
        Arguments.arguments(
            "빙티", 4, 3, "면담"
        ),
        Arguments.arguments(
            "이든", 5, 2, "면담"
        ),
        Arguments.arguments(
            "빙봉", 6, 1, "면담"
        ),
        Arguments.arguments(
            "쿠키", 3, 2, "면담"
        ),
        Arguments.arguments(
            "짱수", 0, 2, "경고"
        )
    );
  }
}
