package cityrescue;

import cityrescue.enums.UnitType;

public class FireEngine extends Unit{
    //4 ticks to resolve incident
    public FireEngine(int unitId, int stationId){
        super();
        this.unitType = UnitType.FIRE_ENGINE;
    }
}
