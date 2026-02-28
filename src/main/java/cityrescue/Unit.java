package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public abstract class Unit {    
    
    private final int unitId;
    private final UnitType unitType;

    private int homeStationId; // spec says to have each unit be assigned a home station id 

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

    public int getHomeStationId(){
        return homeStationId;
    }

    public void setHomeStationId(int stationId) {
        this.homeStationId = stationId;
    }

    public int[] getLocation(){
        return new int[]{x, y};
    }

    // some methods for geting the unit's x and y which will be needed for calculating manhattan distance 
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public UnitStatus getStatus(){
        return status;
    }

    public void setStatus(UnitStatus status){
        this.status = status;
    }

    //for resetting the unit so its now no longet assigned to an incedent 
    public void clearAssignment() {
        this.assignedIncidentId = -1;
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
}
