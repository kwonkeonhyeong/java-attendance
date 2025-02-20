package attendance.view.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

public class InputView {

    private static final Scanner SCANNER = new Scanner(System.in);

    public Command inputCommand() {
        System.out.print(getToday());
        System.out.println(" 기능을 선택해주세요.");

        Arrays.stream(Command.values())
                .forEach(command -> System.out.println(command.getOption() + ". " + command.getDescription()));

        return Command.from(SCANNER.nextLine());
    }

    public String inputNickname() {
        System.out.println("닉네임을 입력해주세요.");
        return SCANNER.nextLine();
    }

    public LocalDateTime inputAttendanceTime() {
        try {
            System.out.println("등교 시간을 입력해주세요.");
            return LocalDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.parse(SCANNER.nextLine()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("올바른 시간 형식이 아닙니다.");
        }
    }

    public String inputUpdateCrewName() {
        System.out.println("출석을 수정하려는 크루의 닉네임을 입력해주세요.");
        return SCANNER.nextLine();
    }

    public LocalDate inputUpdateAttendanceDate() {
        System.out.println("수정하려는 날짜(일)를 입력해주세요.");
        return LocalDate.of(2024, 12, Integer.parseInt(SCANNER.nextLine()));
    }

    public LocalTime inputUpdateAttendanceTime() {
        System.out.println("언제로 변경하겠습니까?");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(SCANNER.nextLine(), formatter);
    }

    private String getToday() {
        LocalDate now = LocalDate.of(2024, 12, 14);
        KoreaDayOfWeek dayOfWeek = KoreaDayOfWeek.from(now.getDayOfWeek());
        return String.format("오늘은 %d월 %d일 %s입니다.",
                now.getMonth().getValue(),
                now.getDayOfMonth(),
                dayOfWeek.getName()
        );
    }
}
