package attendance.model.repository;

import attendance.model.domain.crew.TimeLogs;
import attendance.model.domain.crew.TimeLog;
import attendance.model.domain.crew.Crew;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class CrewAttendanceDeserializer {

  public Map<Crew, TimeLogs> readAll(final Path filePath) throws UncheckedIOException {
    try (Stream<String> lines = Files.lines(filePath)) {
      HashMap<Crew, TimeLogs> map = new HashMap<>();
      lines.skip(1)
          .map(this::deSerialize)
          .forEach(entry -> {
            TimeLogs times = map.getOrDefault(entry.getKey(), new TimeLogs(new LinkedList<>()));
            times.add(entry.getValue());
            map.put(entry.getKey(), times);
          });

      return map;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Entry<Crew, TimeLog> deSerialize(final String line) throws UncheckedIOException {
    String datetime = line.split(",")[1];
    LocalDateTime parsedDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        .parse(datetime, LocalDateTime::from);
    return new SimpleImmutableEntry<>(new Crew(line.split(",")[0]), TimeLog.from(parsedDateTime));
  }

}
