package attendance;

import java.util.Objects;

public class Crew {

  private static final int MAX_LENGTH = 4;
  private static final int MIN_LENGTH = 2;

  private final String name;

  public Crew(String name) {
    validateNameLength(name);
    this.name = name;
  }

  private void validateNameLength(String name) {
    if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
      throw new IllegalArgumentException();
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

}
