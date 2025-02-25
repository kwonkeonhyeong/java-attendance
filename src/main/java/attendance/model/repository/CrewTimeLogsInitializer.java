package attendance.model.repository;

import attendance.model.domain.crew.Crew;
import attendance.model.domain.crew.TimeLogs;
import java.nio.file.Path;
import java.util.Map;

public interface CrewTimeLogsInitializer {
  Map<Crew, TimeLogs> initialize(Path filePath);
}
