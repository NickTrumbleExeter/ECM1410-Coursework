package cityrescue;

import cityrescue.enums.UnitType;

public class PoliceCar extends Unit{
    //3 ticks to resolve incident

    public PoliceCar(int unitId, int stationId){
        super();
        this.unitType = UnitType.POLICE_CAR;
    }
}
