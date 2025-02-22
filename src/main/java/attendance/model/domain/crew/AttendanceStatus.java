package attendance.model.domain.crew;

import java.util.Arrays;

public enum AttendanceStatus {

  ATTENDANCE("출석"),
  LATE("지각"),
  ABSENCE("결석");

  private final String name;

  AttendanceStatus(String name) {
    this.name = name;
  }

  public static AttendanceStatus from(String name) {
    return Arrays.stream(values())
        .filter(attendanceStatus -> attendanceStatus.name.equals(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("해당하는 출석 상태가 없습니다."));
  }

  public String getName() {
    return name;
  }

}
