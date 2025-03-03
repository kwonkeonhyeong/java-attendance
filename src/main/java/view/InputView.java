package view;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class InputView {

  private final Scanner scanner = new Scanner(System.in);

  public String printCommand(LocalDate date) {
    System.out.printf(
        """
            오늘은 %d월 %d일 %s입니다. 기능을 선택해 주세요.
            1. 출석 확인
            2. 출석 수정
            3. 크루별 출석 기록 확인
            4. 제적 위험자 확인
            Q. 종료
            """,
        date.getMonthValue(),
        date.getDayOfMonth(),
        date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN));
    return scanner.nextLine();
  }
}
