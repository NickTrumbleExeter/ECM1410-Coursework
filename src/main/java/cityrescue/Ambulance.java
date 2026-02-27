package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

//2 ticks to resolve incident
public class Ambulance extends Unit{

    public Ambulance(int stationId){
        super();
        this.unitType = UnitType.AMBULANCE;
    }

    @Override
    public boolean canHandle(IncidentType incidentType){
        return incidentType == IncidentType.MEDICAL;
    }
}
