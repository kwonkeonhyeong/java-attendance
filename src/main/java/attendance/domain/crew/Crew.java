package attendance.domain.crew;

import java.util.Objects;

public class Crew {

  private final String name;

  public Crew(String name) {
    validate(name);
    this.name = name;
  }

  private void validate(String value) {
    if (value.length() < 2 || value.length() > 4) {
      throw new IllegalArgumentException("크루 이름은 2 ~ 4글자 사이여야 합니다");
    }
  }

  public String getName() {
    return name;
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
