package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;
import cityrescue.exceptions.InvalidSeverityException;

/**
 *  Incident class
 *  Handles all methods and stores
 *  all information to do with the 
 *  City incidents
 */
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

    /**
     *  initialises a new incident in the city
     *  with parameters provided
     * 
     * @param incidentId ID of new incident
     * @param type type of new incident
     * @param severity severity of new incident
     * @param x x coordinate of new incident
     * @param y y coordinate of new incident
     */
    public Incident(int incidentId, IncidentType type, int severity, int x, int y){
        this.incidentId = incidentId;
        this.type = type;

        this.status = IncidentStatus.REPORTED;
        this.assignedUnitId = -1;
        this.ticksRemaining = (type == IncidentType.FIRE) ? 4 : (type == IncidentType.CRIME) ? 3 : 2;

        this.severity = severity;
        this.x = x;
        this.y = y;
    }

    /**
     * returns the type of incident
     * 
     * @return returns type of incident of type IncidentType
     */
    public IncidentType getType(){
        return type;
    }

    /**
     * returns incident status
     * 
     * @return returns status of incident type IncidentStatus
     */
    public IncidentStatus getIncidentStatus(){
        return status;
    }

    /**
     * changes status to new status provided
     * in params
     * 
     * @param newStatus status to update to
     */

    public void updateStatus(IncidentStatus newStatus){
        status = newStatus;
    }
    
    /**
     * returns incident severity
     * 
     * @return returns severity of incident of type int
     */
    public int getIncidentSeverity(){
        return severity;
    }

    /**
     * returns location of incident
     * 
     * @return returns int array containing location of incident
     */
    public int[] getLocation(){
        return new int[] {x, y};
    }

    /**
     * returns x coordinate of incident
     * 
     * @return integer x coordinate of incident
     */
    public int getX(){
        return x;
    }

    /**
     * returns y coordinate of incident
     * 
     * @return integer y coordinate of incident
     */
    public int getY(){
        return y;
    }

    /**
     * returns how many ticks until incident is solved
     * 
     * @return integer how many ticks remaining
     */
    public int getTicksRemaining(){
        return ticksRemaining;
    }

    /**
     * decrements how many ticks remaining for the
     * incident to be resolved, given there are more
     * than 0 ticks remaining
     */
    public void resolveTick() {
        if (ticksRemaining > 0) ticksRemaining--;
    }

    /**
     * updates the severity of the incident to the
     * new severity provided
     * 
     * @param newSeverity new severity to update to
     * @throws InvalidSeverityException if severity outside [0, 5]
     */
    public void updateSeverity(int newSeverity) throws InvalidSeverityException{
        if (newSeverity < 1 || newSeverity > 5)
            throw new InvalidSeverityException("invalid severity");
        this.severity = newSeverity;
    }

    /**
     * returns ID of incident
     * 
     * @return integer ID of incident
     */
    public int getIncidentId(){
        return incidentId;
    }

    /**
     * returns the ID of the unit assigned to this incident
     * 
     * @return integer ID of unit assigned
     */
    public int getAssignedUnit(){
        return assignedUnitId;
    }

    /**
     * updates the assigned unit ID to new one provided
     * 
     * @param unitId new ID to update to
     * @param ticksToResolve how many ticks remaining 
     */
    public void setAssignedUnit(int unitId, int ticksToResolve){
        this.assignedUnitId = unitId;
        this.ticksRemaining = ticksToResolve;
    }

    /**
     * resets Assigned unit ID to -1 
     * and the ticks remaining to 0;
     */
    public void clearAssignedUnit(){
        this.assignedUnitId = -1;
        this.ticksRemaining = 0;
    }

}
