package attendance;

import java.time.LocalDateTime;

public interface AttendanceRecord {
  boolean isExists();
  LocalDateTime getRecord();
}
