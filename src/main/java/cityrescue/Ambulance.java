package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

//2 ticks to resolve incident
public class Ambulance extends Unit{

    public Ambulance(int unitId, int homeStationId, int startX, int startY){
        super(unitId, UnitType.AMBULANCE, homeStationId, startX, startY);
    }

    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.MEDICAL;
    }

    @Override
    public int getTicksToResolve(int severity) {
        return 2;
    }
}
