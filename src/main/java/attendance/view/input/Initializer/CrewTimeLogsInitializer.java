package attendance.view.input.Initializer;

import attendance.domain.crew.Crew;
import attendance.domain.crew.TimeLogs;
import java.nio.file.Path;
import java.util.Map;

public interface CrewTimeLogsInitializer {

  Map<Crew, TimeLogs> initialize(Path filePath);

}
