package view;

import static controller.AttendanceController.GLOBAL_DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class InputView {

  private final Scanner scanner = new Scanner(System.in);

  public String printCommand(LocalDate date) {
    System.out.printf(
        """
            %n오늘은 %d월 %d일 %s입니다. 기능을 선택해 주세요.
            1. 출석 확인
            2. 출석 수정
            3. 크루별 출석 기록 확인
            4. 제적 위험자 확인
            Q. 종료%n
            """,
        date.getMonthValue(),
        date.getDayOfMonth(),
        date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN));
    return scanner.nextLine();
  }

  public String printInputCrewNickName() {
    System.out.printf("%n닉네임을 입력해 주세요.%n");
    String nickname = scanner.nextLine();
    validateInputEmpty(nickname);
    return nickname;
  }

  public LocalDateTime printInputAttendanceTime() {
    System.out.printf("%n등교 시간을 입력해 주세요.%n");
    try {
      LocalTime localTime = LocalTime.parse(scanner.nextLine());
      return LocalDateTime.of(GLOBAL_DATE, localTime);
    } catch (DateTimeParseException exception) {
      throw new IllegalArgumentException("올바른 시간 형식이 아닙니다.");
    }
  }

  public int printInputModifyDate() {
    System.out.printf("%n수정하려는 날짜(일)를 입력해 주세요.%n");
    String date = scanner.nextLine();
    validateNumeric(date);
    return Integer.parseInt(date);
  }

  public LocalTime printInputModifyTime() {
    System.out.printf("%n언제로 변경하겠습니까?%n");
    try {
      return LocalTime.parse(scanner.nextLine());
    } catch (DateTimeParseException exception) {
      throw new IllegalArgumentException("올바른 시간 형식이 아닙니다.");
    }
  }

  private void validateInputEmpty(String input) {
    if (input == null || input.isBlank()) {
      throw new IllegalArgumentException("입력 형식을 확인해주세요");
    }
  }

  private void validateNumeric(String input) {
    if (!input.matches("^-?\\d+$")) {
      throw new IllegalArgumentException("입력 형식을 확인해주세요");
    }
  }

}
