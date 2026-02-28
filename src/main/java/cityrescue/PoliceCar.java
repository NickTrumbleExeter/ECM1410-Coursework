package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

//3 ticks to resolve incident
public class PoliceCar extends Unit{
    
    public PoliceCar(int unitId, int homeStationId, int startX, int startY){
        super(unitId, UnitType.POLICE_CAR, homeStationId, startX, startY);
    }

    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.CRIME;
    }

    @Override
    public int getTicksToResolve(int severity) {
        return 3;
    }
}
