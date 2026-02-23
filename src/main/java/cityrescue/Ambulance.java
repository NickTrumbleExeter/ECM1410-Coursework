package cityrescue;

import cityrescue.enums.UnitType;

public class Ambulance extends Unit{
    //2 ticks to resolve incident

    public Ambulance(int unitId, int stationId){
        super();
        this.unitType = UnitType.AMBULANCE;
    }
}
