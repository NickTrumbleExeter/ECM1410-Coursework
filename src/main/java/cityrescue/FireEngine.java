package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

//4 ticks to resolve incident
public class FireEngine extends Unit{

    public FireEngine(int unitId, int homeStationId, int startX, int startY){
        super(unitId, UnitType.FIRE_ENGINE, homeStationId, startX, startY);
    }

    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.FIRE;
    }

    @Override
    public int getTicksToResolve(int severity) {
        return 4;
    }
}
