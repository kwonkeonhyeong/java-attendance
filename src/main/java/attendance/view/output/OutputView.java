package attendance.view.output;

import attendance.dto.AttendanceLogResponse;
import attendance.dto.CrewAttendanceLogResponse;
import attendance.dto.RequiresManagementCrewResponse;
import attendance.dto.UpdateAttendanceResponse;
import attendance.view.input.KoreaDayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class OutputView {

    public void printAttendanceLog(AttendanceLogResponse response) {
        String message = String.format("%d월 %d일 %s %s (%s)",
                response.getDate().getMonthValue(),
                response.getDate().getDayOfMonth(),
                KoreaDayOfWeek.from(response.getDate().getDayOfWeek()).getName(),
                formatTime(response.getTime()),
                response.getAttendanceStatus());
        System.out.println(message);
    }

    public void printUpdateAttendanceResponse(UpdateAttendanceResponse updateAttendanceResponse) {
        String message = String.format("%d월 %d일 %s %s (%s) -> %s (%s) 수정 완료!",
                updateAttendanceResponse.getBefore().getMonthValue(),
                updateAttendanceResponse.getBefore().getDayOfMonth(),
                KoreaDayOfWeek.from(updateAttendanceResponse.getBefore().getDayOfWeek()).getName(),
                formatTime(updateAttendanceResponse.getBefore().toLocalTime()),
                updateAttendanceResponse.getBeforeStatus(),
                formatTime(updateAttendanceResponse.getAfter().toLocalTime()),
                updateAttendanceResponse.getAfterStatus());
        System.out.println(message);
    }

    public void printCrewAttendanceLogResponse(CrewAttendanceLogResponse crewAttendanceLogResponse) {
        System.out.printf("이번 달 %s의 출석 기록입니다.%n%n", crewAttendanceLogResponse.getCrewName());

        crewAttendanceLogResponse.getTimeLogs().forEach(this::printAttendanceLog);
        System.out.println();
        System.out.printf("출석: %d회%n", crewAttendanceLogResponse.getAttendanceCount());
        System.out.printf("지각: %d회%n", crewAttendanceLogResponse.getLateCount());
        System.out.printf("결석: %d회%n", crewAttendanceLogResponse.getAbsenceCount());

        if (!crewAttendanceLogResponse.getManagementStatus().equals("일반")) {
            System.out.printf("%n%s 대상자입니다.%n%n", crewAttendanceLogResponse.getManagementStatus());
        }
    }

    public void printManagementCrews(List<RequiresManagementCrewResponse> responses) {
        System.out.println("\n제적 위험자 조회 결과");
        for (RequiresManagementCrewResponse response : responses) {
            System.out.println(formatManagementCrew(response));
        }
        System.out.println();
    }

    private String formatManagementCrew(RequiresManagementCrewResponse response) {
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
