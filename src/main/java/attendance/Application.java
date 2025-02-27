package attendance;

import attendance.domain.AttendanceManager;

public class Application {

  public static void main(String[] args) {

    AttendanceManager attendanceManager = new AttendanceManager();
    attendanceManager.startAttendanceProcess();

  }

}
