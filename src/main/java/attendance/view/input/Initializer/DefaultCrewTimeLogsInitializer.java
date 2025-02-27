package attendance.view.input.Initializer;

import attendance.domain.crew.Crew;
import attendance.domain.crew.TimeLog;
import attendance.domain.crew.TimeLogs;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCrewTimeLogsInitializer implements CrewTimeLogsInitializer {

  @Override
  public Map<Crew, TimeLogs> initialize(Path filePath) {
    try (Stream<String> lines = Files.lines(filePath)) {
      Map<Crew, List<TimeLog>> crewWithTimeLogs = groupByCrewWithTimeLogs(lines);
      return crewWithTimeLogs.entrySet().stream()
          .collect(Collectors.toMap(
              Entry::getKey,
              entry -> new TimeLogs(entry.getValue())
          ));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Map<Crew, List<TimeLog>> groupByCrewWithTimeLogs(Stream<String> dataStream) {
    return dataStream.skip(1)
        .map(this::deSerialize)
        .collect(Collectors.groupingBy(
            Entry::getKey,
            Collectors.mapping(Entry::getValue, Collectors.toList())
        ));
  }

  private Entry<Crew, TimeLog> deSerialize(final String line) throws UncheckedIOException {
    String datetime = line.split(",")[1];
    LocalDateTime parsedDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        .parse(datetime, LocalDateTime::from);
    return new SimpleImmutableEntry<>(new Crew(line.split(",")[0]), TimeLog.from(parsedDateTime));
  }

}
