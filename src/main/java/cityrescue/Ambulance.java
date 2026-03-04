package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

/** Ambulance class
*   Extends from parent class Unit
*   Handles all methods and stores
*   all information about the ambulances
*   specifically.
*/
//2 ticks to resolve incident
public class Ambulance extends Unit{

    /**
     * initialises a new Ambulance in station that has Id provided
     * 
     * @param unitId ID of ambulance
     * @param homeStationId ID of the medical station ambulance is stationed at
     * @param startX x coordinate of home station
     * @param startY y coordinate of home station
     */
    public Ambulance(int unitId, int homeStationId, int startX, int startY){
        super(unitId, UnitType.AMBULANCE, homeStationId, startX, startY);
    }

    /**
     * checks if the ambulance can handle the type
     * of incident provided in arguament 
     * 
     * @param type incident type testing handling
     * 
     * @return boolean; true if can handle(Medical incident)
     */
    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.MEDICAL;
    }

    /**
     * returns the amount of ticks to resolve any
     * incident of type it can handle.
     * 
     * @param severity the severity of the incident
     * 
     * @return returns how many ticks to resolve (2)
     */
    @Override
    public int getTicksToResolve(int severity) {
        return 2;
    }
}
