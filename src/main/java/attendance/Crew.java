package attendance;

public class Crew {

  public Crew(String name) {
    if (name.length() < 2 || name.length() > 4) {
      throw new IllegalArgumentException();
    }
  }

}
