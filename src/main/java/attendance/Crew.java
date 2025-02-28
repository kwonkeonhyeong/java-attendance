package attendance;

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

}
