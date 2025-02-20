package attendance.model.domain.crew;

import java.util.Objects;

public class Crew {

    private final CrewName name;

    private Crew(CrewName name) {
        this.name = name;
    }

    public static Crew from(String name) {
        return new Crew(CrewName.from(name));
    }

    public String getName() {
        return name.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Crew crew = (Crew) o;
        return Objects.equals(name, crew.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Crew{" +
                "name='" + name + '\'' +
                '}';
    }
}
