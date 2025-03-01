package attendance;

import java.util.Objects;

public class Crew {

  private final String name;

  public Crew(String name) {
    validateNameLength(name);
    this.name = name;
  }

  private void validateNameLength(String name) {
    if (name.length() < 2 || name.length() > 4) {
      throw new IllegalArgumentException();
    }
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

}
