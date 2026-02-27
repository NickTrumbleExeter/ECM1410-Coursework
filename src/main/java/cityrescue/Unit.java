package cityrescue;

import cityrescue.enums.*;

public abstract class Unit {    
    private final int unitId;

    private UnitType unitType;
    private UnitStatus status;
    private int stationId;
    private int x;
    private int y;

    public static int unitIdCount = 0;

    public Unit(UnitType type, int stationId){
        this.unitId = unitIdCount;
        this.stationId = stationId;
        this.unitType = type;
        this.status = UnitStatus.IDLE;
        unitIdCount++;
    }

    public int getUnitId(){
        return unitId;
    }

    public UnitType getUnitType(){
        return unitType;
    }

    public int getStationId(){
        return stationId;
    }

    public int[] getLocation(){
        return new int[]{x, y};
    }

    public UnitStatus getStatus(){
        return status;
    }

    public void updateStationId(int newStationId){
        stationId = newStationId;
    }

    public void updateStatus(UnitStatus status){
        this.status = status;
    }

    abstract boolean canHandle(IncidentType incidentType);

    abstract int getTicksToResolve(int severity);
}
