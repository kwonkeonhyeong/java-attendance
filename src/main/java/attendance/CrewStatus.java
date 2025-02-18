package attendance;

public class CrewStatus {

    private final int absenceCount;
    private final int lateCount;
    private final String dangerStatus;

    private CrewStatus(int absenceCount, int lateCount, String dangerStatus) {
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.dangerStatus = dangerStatus;
    }

    public static CrewStatus of(int absenceCount, int lateCount) {
        return new CrewStatus(absenceCount, lateCount, "면담");
    }

    public boolean isDanger() {
        return false;
    }
}
