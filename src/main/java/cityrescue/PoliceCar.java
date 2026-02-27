package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

//3 ticks to resolve incident
public class PoliceCar extends Unit{
    
    public PoliceCar(int stationId){
        super();
        this.unitType = UnitType.POLICE_CAR;
    }

    @Override
    public boolean canHandle(IncidentType incidentType){
        return incidentType == IncidentType.CRIME;
    }
}
