package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

/**
 * abstract class for the unit 
 * contains the unit information:
 * - unit ID 
 * - unit type 
 * - home station ID
 * - coordinates 
 * - status 
 * - assigned incident ID 
 */
public abstract class Unit {    
    
    private final int unitId;
    private final UnitType unitType;

    private int homeStationId;

    private int x;
    private int y;

    private UnitStatus status;

    private int assignedIncidentId; // -1 if none as -1 is ouside of ID range (IDs can only be >0)

    protected Unit(int unitId, UnitType unitType, int homeStationId, int startX, int startY) {
        this.unitId = unitId;
        this.unitType = unitType;
        this.homeStationId = homeStationId;
        this.x = startX;
        this.y = startY;
        this.status = UnitStatus.IDLE;
        this.assignedIncidentId = -1;
    }

    public int getUnitId(){
        return unitId;
    }

    public UnitType getUnitType(){
        return unitType;
    }

    /**
     * returns the ID of the home satation of a unit 
     * 
     * @return returns an int of the ID 
     */
    public int getHomeStationId(){
        return homeStationId;
    }

    /**
     * sets the home station of a unit to a specified station
     * 
     * @param stationId the ID of the new home station the unit will be set to 
     */
    public void setHomeStationId(int stationId) {
        this.homeStationId = stationId;
    }

    /**
     * returns the location of a desired unit 
     * 
     * @return returs a list [x,y] of the coordinates of teh unit 
     */
    public int[] getLocation(){
        return new int[]{x, y};
    }

    /**
     * moves a unit in a desired direction 
     * 
     * @param dir int value added to the existing x and y coordinate 
     */
    public void moveUnit(int[] dir){
        x += dir[0];
        y += dir[1];
        //throw new RuntimeException("x: " + Integer.toString(x) + " y: " + Integer.toString(y) + "id: " + Integer.toString(unitId));
    }

    // some methods for geting the unit's x and y which will be needed for calculating manhattan distance 
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * checks if a unit ha arrived at a given location 
     * 
     * @param LOC x and y coordinates of teh desired location 
     * @return returns a boolean depending on if teh units x and y coordinates match the location coordinates
     */
    public boolean hasArrived(int[] LOC){
        return x == LOC[0] && y == LOC[1];
    }

    /**
     * sets the location of a unit 
     * 
     * @param x x coordinate of the unit 
     * @param y y coordinate of the unit 
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * retrieves the status of a unit 
     * @return the status of a unit 
     */
    public UnitStatus getStatus(){
        return status;
    }

    /**
     * changes the status of a unit to a given status 
     * @param status teh new status the unit will be changed to 
     */
    public void setStatus(UnitStatus status){
        this.status = status;
    }

    /**
     * for resetting the unit so its now no longet assigned to an incedent 
     */
    public void clearAssignment() {
        this.assignedIncidentId = -1;
        this.status = UnitStatus.IDLE;
    }

    // methods for getting and assigning incedent IDs
        public int getAssignedIncidentId() {
        return assignedIncidentId;
    }

    public void setAssignedIncidentId(int incidentId) {
        this.assignedIncidentId = incidentId;
    }

    public abstract boolean canHandle(IncidentType type);

    public abstract int getTicksToResolve(int severity);

    /**
     * calculates the manhatten distance between the unit and the incident 
     * 
     * @param unitLOC x and y coordinates of the unit 
     * @param incidentLOC x and y coordinate of the incident 
     * @return returns the int for the manhatten distance 
     */
    public int manhattenDist(int[] unitLOC, int[] incidentLOC){
        int absX = Math.abs(unitLOC[0] - incidentLOC[0]);
        int absY = Math.abs(unitLOC[1] - incidentLOC[1]);
        
        return absX + absY;
    }
}
