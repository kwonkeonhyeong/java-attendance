package attendance.view.output;

import attendance.model.domain.Information.AttendanceInformation;
import attendance.model.domain.Information.CrewAttendanceInformation;
import attendance.model.domain.Information.ManagementCrewInformation;
import attendance.model.domain.Information.AttendanceUpdatesInformation;
import attendance.view.input.KoreaDayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class OutputView {

    public void printAttendanceLog(AttendanceInformation information) {
        String message = String.format("%d월 %d일 %s %s (%s)",
                information.getDate().getMonthValue(),
                information.getDate().getDayOfMonth(),
                KoreaDayOfWeek.from(information.getDate().getDayOfWeek()).getName(),
                formatTime(information.getTime()),
                information.getAttendanceStatus());
        System.out.println(message);
    }

    public void printAttendanceUpdatesInformation(
        AttendanceUpdatesInformation attendanceUpdatesInformation) {
        String message = String.format("%d월 %d일 %s %s (%s) -> %s (%s) 수정 완료!",
                attendanceUpdatesInformation.getBefore().getMonthValue(),
                attendanceUpdatesInformation.getBefore().getDayOfMonth(),
                KoreaDayOfWeek.from(attendanceUpdatesInformation.getBefore().getDayOfWeek()).getName(),
                formatTime(attendanceUpdatesInformation.getBefore().toLocalTime()),
                attendanceUpdatesInformation.getBeforeStatus(),
                formatTime(attendanceUpdatesInformation.getAfter().toLocalTime()),
                attendanceUpdatesInformation.getAfterStatus());
        System.out.println(message);
    }

    public void printCrewAttendanceInformation(CrewAttendanceInformation crewAttendanceInformation) {
        System.out.printf("이번 달 %s의 출석 기록입니다.%n%n", crewAttendanceInformation.getCrewName());

        crewAttendanceInformation.getAttendanceInformation().forEach(this::printAttendanceLog);
        System.out.println();
        System.out.printf("출석: %d회%n", crewAttendanceInformation.getAttendanceCount());
        System.out.printf("지각: %d회%n", crewAttendanceInformation.getLateCount());
        System.out.printf("결석: %d회%n", crewAttendanceInformation.getAbsenceCount());

        if (!crewAttendanceInformation.getManagementStatus().equals("일반")) {
            System.out.printf("%n%s 대상자입니다.%n%n", crewAttendanceInformation.getManagementStatus());
        }
    }

    public void printManagementCrewInformation(List<ManagementCrewInformation> information) {
        System.out.println("\n제적 위험자 조회 결과");
        for (ManagementCrewInformation managementCrewInformation : information) {
            System.out.println(formatManagementCrew(managementCrewInformation));
        }
        System.out.println();
    }

    private String formatManagementCrew(ManagementCrewInformation response) {
        return String.format(
            "- %s: 결석 %d회, 지각 %d회 (%s)",
            response.getCrewName(),
            response.getAbsenceCount(),
            response.getLateCount(),
            response.getManagementStatus()
        );
    }

    public String formatTime(LocalTime time) {
        if (time == null) {
            return "--:--";
        }
        return String.format("%2d:%2d", time.getHour(), time.getMinute());
    }

}
