package cityrescue;

import cityrescue.enums.*;

public abstract class Unit {
    private final int stationId;
    private final int unitId;

    private UnitType unitType;
    private UnitStatus status;
    private int x;
    private int y;

    public Unit(int unitId, int stationId){
        this.unitId = unitId;
        this.stationId = stationId;
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

    abstract boolean canHandle(IncidentType incidentType);

    abstract int getTicksToResolve(int severity);
}
