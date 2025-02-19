package attendance;

public class DangerCrewResponse {
    private final String name;
    private final int absenceCount;
    private final int lateCount;
    private final String dangerStatus;

    private DangerCrewResponse(String name, int absenceCount, int lateCount, String dangerStatus) {
        this.name = name;
        this.absenceCount = absenceCount;
        this.lateCount = lateCount;
        this.dangerStatus = dangerStatus;
    }

    public static DangerCrewResponse from(CrewAttendance crewAttendance) {
        return new DangerCrewResponse(
                crewAttendance.getCrew().getName(),
                crewAttendance.getAttendanceStatus().getAbsenceCount(),
                crewAttendance.getAttendanceStatus().getLateCount(),
                crewAttendance.getAttendanceStatus().getDangerStatus().getName()
        );
    }

    public String getName() {
        return name;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public String getDangerStatus() {
        return dangerStatus;
    }

    @Override
    public String toString() {
        return "DangerCrewResponse{" +
                "name='" + name + '\'' +
                ", absenceCount=" + absenceCount +
                ", lateCount=" + lateCount +
                ", dangerStatus='" + dangerStatus + '\'' +
                '}' + '\n';
    }
}
