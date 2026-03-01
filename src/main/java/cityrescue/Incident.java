package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;

public class Incident {
    //ambulance = 2 ticks, police car = 3, fire engine = 4
    private IncidentType type;
    private IncidentStatus status;
    private int severity;
    private int incidentId;
    private int x;
    private int y;
    private int assignedUnit;
    private int ticksToResolve;

    public static int incidentCount = 0;

    public Incident(IncidentType type, int severity, int x, int y){
        this.type = type;
        ticksToResolve = (type == IncidentType.FIRE) ? 4 : (type == IncidentType.CRIME) ? 3 : 2;

        status = IncidentStatus.REPORTED;
        this.severity = severity;
        this.x = x;
        this.y = y;

        assignedUnit = -1;
        incidentCount++;
    }

    public IncidentType getType(){
        return type;
    }

    public IncidentStatus getIncidentStatus(){
        return status;
    }

    public void updateStatus(IncidentStatus newStatus){
        status = newStatus;
    }

    public int getIncidentSeverity(){
        return severity;
    }

    public int[] getLocation(){
        return new int[] {x, y};
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getTicksToResolve(){
        return ticksToResolve;
    }

    public void updateSeverity(int newSeverity){
        if (newSeverity <= 5 && newSeverity >= 1)
            severity = newSeverity;
        else
            throw new InvalidSeverityException();
    }

    public int getIncidentId(){
        return incidentId;
    }

    public int getAssignedUnit(){
        return assignedUnit;
    }

    public void setAssignedUnit(int unitId){
        assignedUnit = unitId;
    }

    public void resolveTick(){
        ticksToResolve--;
    }
}
