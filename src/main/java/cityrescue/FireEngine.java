package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

/**
 * FireEngine class
 * Extends from parent class Unit
 * Handles all methods and stores
 * all data to do with Fire Engine
 */
//4 ticks to resolve incident
public class FireEngine extends Unit{

    /**
     * initialises a new Fire Engine in station 
     * with ID provided
     * 
     * @param unitId ID of fire engine
     * @param homeStationId ID of Fire department station
     * @param startX x coordinate of fire department
     * @param startY y coordinate of fire department
     */
    public FireEngine(int unitId, int homeStationId, int startX, int startY){
        super(unitId, UnitType.FIRE_ENGINE, homeStationId, startX, startY);
    }

    /**
     * checks if the fire engine can handle the 
     * type of incident provided in arguament 
     * 
     * @param type incident type testing handling
     * 
     * @return boolean; true if can handle(fire incident)
     */
    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.FIRE;
    }

    /**
     * returns the amount of ticks to resolve any
     * incident of type it can handle.
     * 
     * @param severity the severity of the incident
     * 
     * @return returns how many ticks to resolve (4)
     */
    @Override
    public int getTicksToResolve(int severity) {
        return 4;
    }
}
