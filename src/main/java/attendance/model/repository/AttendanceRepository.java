package attendance.model.repository;

import attendance.model.domain.crew.Crew;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AttendanceRepository {

    public final Map<Crew, List<LocalDateTime>> values = new HashMap<>();

    public List<LocalDateTime> findByCrew(Crew crew) {
        validateCrewExistence(crew);
        return values.get(crew).stream()
                .toList();
    }

    public void save(Crew crew, LocalDateTime dateTime) {
        if (existsByCrew(crew)) {
            validateConflict(crew, dateTime);
        }
        List<LocalDateTime> times = values.getOrDefault(crew, new ArrayList<>());
        times.add(dateTime);
        values.put(crew, times);
    }

    public boolean existsByCrew(Crew crew) {
        return values.containsKey(crew);
    }

    public void update(Crew crew, LocalDateTime previousTime, LocalDateTime updatedTime) {
        validateCrewExistence(crew);
        validateDateTimeExistenceByCrew(crew, previousTime);

        values.get(crew).add(updatedTime);
        deleteAttendanceByCrew(crew, previousTime);
    }

    public void deleteAttendanceByCrew(Crew crew, LocalDateTime dateTimeToDelete) {
        validateCrewExistence(crew);
        values.get(crew).remove(dateTimeToDelete);
    }

    public Optional<LocalDateTime> findDateTimeByCrewAndDate(Crew crew, LocalDate date) {
        validateCrewExistence(crew);

        return values.get(crew).stream()
                .filter(time -> time.toLocalDate().equals(date))
                .findFirst();
    }

    public List<Crew> findAllCrews() {
        return values.keySet().stream()
                .toList();
    }

    private void validateCrewExistence(Crew crew) {
        if (!existsByCrew(crew)) {
            throw new IllegalArgumentException("존재하지 않는 크루입니다.");
        }
    }

    private void validateDateTimeExistenceByCrew(Crew crew, LocalDateTime dateTime) {
        values.get(crew).stream()
                .filter(value -> value.isEqual(dateTime))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 크루는 해당 일시의 출석 기록이 없습니다."));
    }

    private void validateConflict(Crew crew, LocalDateTime dateTime) {

        values.get(crew).stream()
                .filter(value -> value.toLocalDate().equals(dateTime.toLocalDate()))
                .findFirst()
                .ifPresent(value -> {
                    throw new IllegalStateException("금일 출석 기록이 이미 존재합니다.");
                });
    }

}
