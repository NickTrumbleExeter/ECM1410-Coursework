package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

//4 ticks to resolve incident
public class FireEngine extends Unit{

    public FireEngine(int stationId){
        super();
    }

    @Override
    public boolean canHandle(IncidentType incidentType){
        return incidentType == IncidentType.FIRE;
    }
}
