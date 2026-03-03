package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;
import cityrescue.exceptions.InvalidSeverityException;

public class Incident {
    //ambulance = 2 ticks, police car = 3, fire engine = 4
    private final IncidentType type;
    private IncidentStatus status;
    private int severity;
    private final int incidentId;
    private final int x;
    private final int y;
    private int assignedUnitId;
    private int ticksRemaining;

    public Incident(int incidentId, IncidentType type, int severity, int x, int y){
        this.incidentId = incidentId;
        this.type = type;

        this.status = IncidentStatus.REPORTED;
        this.assignedUnitId = -1;
        this.ticksRemaining = 0;

        this.severity = severity;
        this.x = x;
        this.y = y;
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

    public int getTicksRemaining(){
        return ticksRemaining;
    }

    public void resolveTick() {
        if (ticksRemaining > 0) ticksRemaining--;
    }

    public void updateSeverity(int newSeverity) throws InvalidSeverityException{
        if (newSeverity < 1 || newSeverity > 5){
            throw new InvalidSeverityException("invalid severity");
        }
        this.severity = newSeverity;
    }

    public int getIncidentId(){
        return incidentId;
    }

    public int getAssignedUnit(){
        return assignedUnitId;
    }

    public void setAssignedUnit(int unitId, int ticksToResolve){
        this.assignedUnitId = unitId;
        this.ticksRemaining = ticksToResolve;
    }

    public void clearAssignedUnit(){
        this.assignedUnitId = -1;
        this.ticksRemaining = 0;
    }

}
