package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

/**
 * Police Car class
 * Extends from parent class Unit
 * Handles all methods and stores
 * all data to do with Police cars
 */
//3 ticks to resolve incident
public class PoliceCar extends Unit{
    
    /**
     * Initialises a new police car in
     * station with ID provided
     * 
     * @param unitId ID of police car
     * @param homeStationId ID of police department station
     * @param startX x coordinate of police department
     * @param startY y coordinate of police department
     */
    public PoliceCar(int unitId, int homeStationId, int startX, int startY){
        super(unitId, UnitType.POLICE_CAR, homeStationId, startX, startY);
    }

    /**
     * checks if the police car can handle the 
     * type of incident provided in arguament 
     * 
     * @param type incident type testing handling
     * 
     * @return boolean; true if can handle(crime incident)
     */
    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.CRIME;
    }

    /**
     * returns the amount of ticks to resolve any
     * incident of type it can handle.
     * 
     * @param severity the severity of the incident
     * 
     * @return returns how many ticks to resolve (3)
     */
    @Override
    public int getTicksToResolve(int severity) {
        return 3;
    }
}
