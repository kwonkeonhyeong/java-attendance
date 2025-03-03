package view.io;

import attendance.AttendanceRecord;
import attendance.AttendanceRecords;
import attendance.Crew;
import attendance.ExistentAttendanceRecord;
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

public class CrewAttendanceRecordInitializer {

  private static final Path FILE_PATH = Path.of("src/main/resources/attendances.csv");

  public Map<Crew, AttendanceRecords> initialize() {
    try (Stream<String> lines = Files.lines(FILE_PATH)) {
      Map<Crew, List<AttendanceRecord>> crewWithTimeLogs = groupByCrewWithTimeLogs(lines);
      return crewWithTimeLogs.entrySet().stream()
          .collect(Collectors.toMap(
              Entry::getKey,
              entry -> new AttendanceRecords(entry.getValue())
          ));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Map<Crew, List<AttendanceRecord>> groupByCrewWithTimeLogs(Stream<String> dataStream) {
    return dataStream.skip(1)
        .map(this::deSerialize)
        .collect(Collectors.groupingBy(
            Entry::getKey,
            Collectors.mapping(Entry::getValue, Collectors.toList())
        ));
  }

  private Entry<Crew, AttendanceRecord> deSerialize(final String line) throws UncheckedIOException {
    String datetime = line.split(",")[1];
    LocalDateTime parsedDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        .parse(datetime, LocalDateTime::from);
    return new SimpleImmutableEntry<>(new Crew(line.split(",")[0]), new ExistentAttendanceRecord(parsedDateTime));
  }

}
