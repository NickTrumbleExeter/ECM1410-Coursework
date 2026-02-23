package cityrescue;

import cityrescue.enums.*;

public abstract class Unit {
    private final int unitId;
    UnitType unitType;
    private final int stationId;

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

    abstract boolean canHandle(IncidentType incidentType);

    abstract int getTicksToResolve(int severity);
}
