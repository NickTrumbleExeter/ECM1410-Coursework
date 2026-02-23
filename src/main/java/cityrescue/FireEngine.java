package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

//4 ticks to resolve incident
public class FireEngine extends Unit{

    public FireEngine(int unitId, int stationId){
        super();
        this.unitType = UnitType.FIRE_ENGINE;
    }

    @Override
    public boolean canHandle(IncidentType incidentType){
        return incidentType == IncidentType.FIRE;
    }
}
