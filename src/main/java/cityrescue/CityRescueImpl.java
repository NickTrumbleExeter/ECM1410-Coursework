package cityrescue;

import java.util.Arrays;

import cityrescue.enums.IncidentStatus;//importing all classes
import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;
import cityrescue.exceptions.CapacityExceededException;
import cityrescue.exceptions.IDNotRecognisedException;
import cityrescue.exceptions.InvalidCapacityException;
import cityrescue.exceptions.InvalidGridException;
import cityrescue.exceptions.InvalidLocationException;
import cityrescue.exceptions.InvalidNameException;
import cityrescue.exceptions.InvalidSeverityException;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */

/**
 * setting maximum values a
 * establishing arrays 
 * establishing IDs 
 */
public class CityRescueImpl implements CityRescue {
    private final int MAX_STATIONS = 20;
    private static final int MAX_UNITS = 50;
    private final int MAX_INCIDENTS = 200;
    private final int DEFAULT_STATION_CAPACITY = 10; //this is a placeholder for a new stations capacity 
                                                     // this is not dfined by the spec, but needs to be done to create a station

    private int stationCount = 0;
    private int nextStationId = 1;

    private int unitCount = 0;
    private int nextUnitId = 1;

    private int incidentCount = 0;
    private int nextIncidentId = 1;

    private int tick = 0;
    private CityMap map;
    private Station[] stations = new Station[MAX_STATIONS];
    private Unit[] units = new Unit[MAX_UNITS];
    private Incident[] incidents = new Incident[MAX_INCIDENTS];

    /**
     * initialises the city map of a specified width and height. 
     * initialises station and incedent arrays, resets tick counter.
     * 
     * @param width width of the city grid
     * @param height height of the city grid 
     * @throws InvalidGridException ,doesnt allow the parameters to be negative 
     */
    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if (width < 0 || height < 0)
            throw new InvalidGridException("Invalid Height for initialisation");

        tick = 0;
        stations = new Station[MAX_STATIONS];
        incidents = new Incident[MAX_INCIDENTS];

        map = new CityMap(width, height);
    }

    /**
     * returns the size of the city grid
     * 
     * @return returns an array of the city grid width and height 
     */
    @Override
    public int[] getGridSize() {
        return new int[]{map.getGridWidth(), map.getGridHeight()};
    }

    /**
     * adds an obsticle to a specified coordinate on the grid.
     * 
     * @param x the x coordinate of the obsticle
     * @param y the y coordinate of the obsticle 
     * @throws InvalidLocationException if given coordinates are outside the grid or in use 
     */
    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (!validLocation(x, y))
            throw new InvalidLocationException("invalid obstacle location (addObstacle)");

        map.addObstacle(x, y);
    }

    /**
     * removes an obsticle to a specified coordinate on the grid.
     * 
     * @param x the x coordinate of the obsticle
     * @param y the y coordinate of the obsticle 
     * @throws InvalidLocationException if given coordinates do not have an obsticle
     */
    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (!validLocation(x, y))
            throw new InvalidLocationException("invalid obstacle location (removeObstacle)");

        map.removeObstacle(x, y);
    }

    /**
     * adds a station to a coordinate on the grid.
     * checks for a blank name, valid location, and number of stations currently on the grid 
     * 
     * @param name name of the station 
     * @param x x coordinate of the station
     * @param y y coordinate of the station
     * 
     * @throws InvalidNameException if name is left blank 
     * @throws InvalidLocationException if the location is blocked or out of bounds 
     * @throws CapacityExceededException if the maximum number of stations has already been reached
     * 
     * @return returns an int of the new station ID
     */
    //IMPLEMENT: setting the coordinate to blocked when adding a station 
    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException, CapacityExceededException {
        //defragmentation?
        if (name.isBlank())
            throw new InvalidNameException("Name cannot be blank");
        if (!map.inBounds(x, y) || map.isBlocked(x, y))
            throw new InvalidLocationException("Location is out of bounds or blocked");
        if (stationCount >= MAX_STATIONS)
            throw new CapacityExceededException("too many");
        int id = nextStationId++;
        
        stations[stationCount++] = new Station(id, name, x, y, DEFAULT_STATION_CAPACITY);
        return id;
    }

    /**
     * removes a station of a specified ID from the grid 
     * 
     * @param stationId ID of the station that is being removed 
     * 
     * @throws IllegalStateException if an attempt is made to remove the station and it stil has units 
     * @throws IDNotRecognisedException if a station with that ID doesnt exist 
     */
    //IDnotrecognised exeption 
    //IMPLEMENT: resetting the coordinate from blocked when removing the station  
    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        if (countUnitsAtStation(stationId) != 0)
            throw new IllegalStateException("Station still has units in it");

        // removeing station from array loop 
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].getStationId() == stationId) {
                for (int j = i; j < stationCount - 1; j++) {
                    stations[j] = stations[j + 1];
                }
                stations[stationCount - 1] = null;
                stationCount--;
                return;
            }
        }

    }

    /**
     * sets the max unit capacity for a specific station 
     * 
     * @param stationId
     * @param maxUnits
     * 
     * @throws IDNotRecognisedException if the specified station does not exist 
     * @throws InvalidCapacityException if  0 > specified capacity < units at station
     */
    // IMPLEMENT: IDNotRecognisedException, InvalidCapacityException
    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        Station station = getStationFromId(stationId);
        station.setCapacity(maxUnits);
    }

    /**
     * returns a sorted list of IDS of all stations currently available 
     * 
     * @return array of all station IDs in ascending order 
     */
    @Override
    public int[] getStationIds() {
        int[] stationIds = new int[stationCount];
        for (int i = 0; i < stationCount; i++){
            stationIds[i] = stations[i].getStationId();
        }
        Arrays.sort(stationIds);
        return stationIds;
    }

    /**
     * adds a new unit of a specified type to a specified station 
     * 
     * @param stationId ID of the station the new unit is being added to 
     * @param type type of unit the new unit wil be 
     * 
     * @throws IDNotRecognisedException if an ID is given of a station that doesnt exist 
     * @throws CapacityExceededException if the station already at capacity
     * @throws IllegalStateException if te station is at the max allowed capacity or no unit type was entered 
     * 
     * @return the ID of the new unit
     */
    // IMPLEMENT: IDNotRecognisedException
    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, CapacityExceededException, IllegalStateException {
        Station station = getStationFromId(stationId);

        if (type == null)
            throw new IllegalStateException("Unit type is null");
        if (unitCount >= MAX_UNITS)
            throw new IllegalStateException("Max units reached");
        if(countUnitsAtStation(stationId) >= station.getCapacity())
            throw new CapacityExceededException("The station is at max capacity");
            
        int id = nextUnitId++;

        Unit unit;
        unit = switch (type) {
            case AMBULANCE -> new Ambulance(id, stationId, station.getX(), station.getY());

            case FIRE_ENGINE -> new FireEngine(id, stationId, station.getX(), station.getY());

            case POLICE_CAR -> new PoliceCar(id, stationId, station.getX(), station.getY());
        };

        units[unitCount++] = unit;
        return id;
    }

    /**
     * removes a unit if its free, reduces unit count 
     * 
     * @param unitID ID of the you to be removed 
     * 
     * @throws IllegalStateException if teh unit is benig used, either en route or at scene 
     */
    //remove IDNotRecognisedException ?
    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = getUnitFromId(unitId);

        if (unit.getStatus() == UnitStatus.EN_ROUTE || unit.getStatus() == UnitStatus.AT_SCENE)
            throw new IllegalStateException("illegal status");

        for (int i = 0; i < MAX_UNITS; i++){
            if (units[i].getUnitId() == unitId){
                units[i] = null;
                break;
            }
        }
    }

    /**
     * moves a unit form a specified station to a new station 
     * 
     * @param unitId ID of the unit that is being moved 
     * @param newStationId ID of the station the unit is being moved to 
     * 
     * @throws IllegalStateException if the unit is being used 
     * @throws CapacityExceededException if the new station is already at capacity 
     */
    // remove IDNotRecognisedException ?
    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException, CapacityExceededException {
        Unit unit = getUnitFromId(unitId);

        if (unit.getStatus() != UnitStatus.IDLE)
            throw new IllegalStateException("invalid status");
        if (countUnitsAtStation(newStationId) >= getStationFromId(newStationId).getCapacity())
            throw new CapacityExceededException("max units reached");

        unit.setHomeStationId(newStationId);        
    }

    /**
     * sets a unit to out of service 
     * 
     * @param unitId ID of the unit that is being put out of service 
     * @param outOfService boolean fo if the unit is currently out of survice 
     * 
     * @throws IllegalStateException if the unit is currently in use 
     */
    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = getUnitFromId(unitId);
        if (outOfService){
            if (unit.getStatus() != UnitStatus.IDLE)
                throw new IllegalStateException("invalid state");
            unit.setStatus(UnitStatus.OUT_OF_SERVICE);
        }
        else {
            unit.setStatus(UnitStatus.IDLE);
        }
    }

    /**
     * returns a list of IDs of all the units 
     * 
     * @return an array of unit IDs in ascending order 
     */
    @Override
    public int[] getUnitIds() {
        int[] unitIds = new int[unitCount];
        for (int j = 0; j < unitCount; j++){
            unitIds[j] = units[j].getUnitId();
        }
        Arrays.sort(unitIds);
        return unitIds;
    }

    /**
     * returns all the information relating to a specified unit 
     * 
     * @param unitId ID of the unit that is being viewed
     * 
     * @throws IDNotRecognisedException if the unit ID doesnt exist 
     * 
     * @return a formatted string of unit information 
     */
    //IMPLEMENT: IDNotRecognisedException
    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        Unit unit = getUnitFromId(unitId);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("U#%d", unitId));
        sb.append(String.format(" TYPE=%s", unit.getUnitType()));
        sb.append(String.format(" HOME=%s", unit.getHomeStationId()));
        int[] location = unit.getLocation();
        sb.append(String.format(" LOC=(%d,%d)", location[0], location[1]));
        sb.append(String.format(" STATUS=%s", unit.getStatus()));
        String incidentId = "";
        if (unit.getAssignedIncidentId() == -1){
            incidentId = "-";
            sb.append(String.format(" INCIDENT=%s", incidentId));
        }
        else{
            incidentId = Integer.toString(unit.getAssignedIncidentId());
            sb.append(String.format(" INCIDENT=%s", incidentId));
            int work = getIncidentFromId(unit.getAssignedIncidentId()).getTicksRemaining();
            sb.append(String.format(" WORK=%d", work));
        }
        return sb.toString();
    }

    /**
     * adds an incedent of a specified type and severity to a coordinate on the grid 
     * 
     * @param type the type of incedent, either MEDICAL, FIRE, CRIME
     * @param severity int of the severity of the new incedent 
     * @param x x coordinate of teh new incedent 
     * @param y y coordinate of the new incedent 
     * 
     * @throws InvalidLocationException if the location selected is blocked or out of bounds 
     * @throws InvalidSeverityException if 1 ≤ severity ≤ 5
     * @throws CapacityExceededException if there are already the max ammount of incedents on the grid 
     * 
     * @return returns the ID of the new incedent 
     */
    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException, CapacityExceededException {
        if (!validLocation(x, y) || map.blocked[x][y])
            throw new InvalidLocationException("Not a valid position");
        if (type == null || severity < 1 || severity > 5)
            throw new InvalidSeverityException("not a valid severity");
        if (incidentCount >= MAX_INCIDENTS)
            throw new CapacityExceededException("too many incidents");

        Incident incident = new Incident(nextIncidentId++, type, severity, x, y);

        incidents[incidentCount++] = incident;
        return incident.getIncidentId();
    }

    /**
     * updates the status of a specified incedent to canceled 
     * 
     * @param incidentId ID of the incedent that is being caneled 
     * 
     * @throws IllegalStateException if case isnt in the state reported or dispached 
     */
    //TODO: maybe neeed to remove the incident from the array?? 
    // remove IDNotRecognisedException
    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        Incident incident = getIncidentFromId(incidentId);
        switch(incident.getIncidentStatus()){
            case REPORTED:
                incident.updateStatus(IncidentStatus.CANCELLED);
                break;
            case DISPATCHED:
                Unit unit = getUnitFromId(incident.getAssignedUnit());
                unit.setStatus(UnitStatus.IDLE);
                incident.updateStatus(IncidentStatus.CANCELLED);
                break;
            default:
                throw new IllegalStateException("Illegal state");
        }
    }

    /**
     * updates the severity of a specified incident
     * 
     * @param incidentId ID of the incident that is being updated 
     * @param newSeverity the severity the incedent is being updated to 
     * 
     * @throws IDNotRecognisedException if an incedent ID is provided that doesnt exist 
     * @throws InvalidSeverityException if the severity is <1 or >5
     * @throws IllegalStateException if the incedent has already been resolved 
     */
    //IMPLEMENT: IDNotRecognisedException
    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        Incident incident = getIncidentFromId(incidentId);

        if (newSeverity < 1 || newSeverity > 5)
            throw new InvalidSeverityException("Invalid severity");

        if (incident.getIncidentStatus() == IncidentStatus.RESOLVED || incident.getIncidentStatus() == IncidentStatus.CANCELLED)
            throw new IllegalStateException("invalid state");

        incident.updateSeverity(newSeverity);
    }

    /**
     * returns a list of all incedent IDs 
     * 
     * @return an array of all the IDs of incedents, past and present. this is in acsending order
     */
    @Override
    public int[] getIncidentIds() {
        int[] incidentIds = new int[incidentCount];
        for (int i = 0; i < incidentCount; i++){
            incidentIds[i] = incidents[i].getIncidentId();
        }
        Arrays.sort(incidentIds);
        return incidentIds;
    }

    /**
     * returns all the information relating to a specific incedent 
     * 
     * @param incidentId ID of the incident being viewed
     * 
     * @throws IDNotRecognisedException if the incident ID doesnt exist 
     * 
     * @return retruns a formatted string of all the incident information
     */
    //IMPLEMENT: IDNotRecognisedException
    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        Incident incident = getIncidentFromId(incidentId);
        String formatted = String.format("I#%d TYPE=%s SEV=%s LOC=(%d,%d) STATUS=%s UNIT=%d", 
            incidentId, incident.getType(), incident.getIncidentSeverity(), incident.getX(),
            incident.getY(), incident.getIncidentStatus(), incident.getAssignedUnit());
        return formatted;
    }

    /**
     * assigns and dispaches available units to unresolved incedents on the grid.
     * for an unresolved incedent the nearest available unit of correct type is assigned ot it.
     * uses Manhattan distance for units and tie breaking rules.
     * updates unit and incedent status when an assignement is made.
     */
    @Override
    public void dispatch() {
        Incident[] reportedIncidents = getIncidents();
        Unit[] availableUnits;
        int[] manhattanDist;

        for (Incident incident : reportedIncidents){
            if (incident.getIncidentStatus() != IncidentStatus.REPORTED)
                continue;

            //find correct units (correct type, not assigned already and not OOS)
            availableUnits = getAvailableUnits(incident.getType());
            manhattanDist = new int[availableUnits.length];

            int shoretestDist = Integer.MAX_VALUE;
            Unit unitToAssign = availableUnits[0];//default unit

            for (int i = 0; i < availableUnits.length; i++){
                //if uncomment(slightly duplicate method), add unitType
                //if (unit.getAssignedIncidentId() != -1 || unit.getUnitType() != unitType || unit.getStatus() == UnitStatus.OUT_OF_SERVICE)
                //  continue;

                int dist = availableUnits[i].manhattenDist(
                    availableUnits[i].getLocation(), incident.getLocation()
                );

                manhattanDist[i] = dist;

                //if statement too long, moved it to other methods
                //assign based off of shortest manhatten, then lower unit id, then lower station id
                if (dist < shoretestDist || isUnitIdLower(availableUnits[i], unitToAssign)){
                    shoretestDist = dist;
                    unitToAssign = availableUnits[i];
                    //add index
                }
            }

            if (shoretestDist != Integer.MAX_VALUE){
                //implies there was a unit found
                incident.updateStatus(IncidentStatus.DISPATCHED);
                unitToAssign.setStatus(UnitStatus.EN_ROUTE);

                unitToAssign.setAssignedIncidentId(incident.getIncidentId());
                incident.setAssignedUnit(unitToAssign.getUnitId(), incident.getTicksRemaining());
            }
        }
    }

    /**
     * advances the system by 1 tick
     * 
     * for each tick cycle the following i sdone:
     * - assigned unit is moved closer to destination 
     * - once unit has arrived it is updated to on scene 
     * - the incedent has its remianing ticks reduced 
     * - once the incedent has no ticks left the units are released and the incedent status is updated to RESOLVED 
     */
    //IN_PROGRES status?
    @Override
    public void tick() {
        tick++;

        Unit[] unitsER = getERUnits();
        Incident[] incidents = getIncidents();
        Incident[] unitAtScenes = new Incident[incidents.length];

        int arrivedCount = 0;

        //1: move en_route units in ascending unit id
        for (Unit unit : unitsER) {
            moveUnit(unit);
            
            //2: mark arrivals
            if (unit.hasArrived(getIncidentFromId(unit.getAssignedIncidentId()).getLocation())){
                unitAtScenes[arrivedCount] = getIncidentFromId(unit.getAssignedIncidentId());
                arrivedCount++;
            }
        }

        
        //3: process on scene work (resolveTick())//not working
        for(Incident incident : unitAtScenes){
            if (incident != null)
                incident.resolveTick();
        }

        //4: resolve completed incidents in ascending incident id
        //maybe remove incident once completed?
        for (Incident incident : incidents){
            if (incident.getTicksRemaining() == 0){
                Unit unitAtScene = getUnitFromId(incident.getAssignedUnit());
                unitAtScene.clearAssignment();
                incident.updateStatus(IncidentStatus.RESOLVED);
            }
        }
    }

    /**
     * returns a formatted sumary of the current system state.
     * gives a snapshot of teh current UI 
     * 
     * @return returns a formatted string of the state of the system 
     */
    @Override
    public String getStatus() {
        int[] incidents = getIncidentIds();
        int[] units = getUnitIds();
        int[] stations = getStationIds();

        StringBuilder sb = new StringBuilder();

        sb.append("TICK=" + tick);
        sb.append(String.format("\nSTATIONS=%d UNITS=%d INCIDENTS=%d OBSTACLES=%d\n", 
            stations.length, units.length, incidents.length, CityMap.getObstacleCount()));
        
        sb.append("INCIDENTS\n");
        for (int incidentID : incidents){
            Incident incident = getIncidentFromId(incidentID);
            String unit = (incident.getAssignedUnit() > 0) ? Integer.toString(incident.getAssignedUnit()) : "-";
            sb.append(String.format("I#%d TYPE=%s SEV=%d LOC=(%d,%d) STATUS=%s UNIT=%s\n", 
                incidentID, incident.getType(), incident.getIncidentSeverity(), incident.getX(), 
                incident.getY(), incident.getIncidentStatus(), unit));
        }        

        sb.append("UNITS\n");
        for (int unitID : units){
            try{
                sb.append(viewUnit(unitID));
                sb.append("\n");
            }
            catch(Exception e){
                System.out.println("Invalid unit id: " + Integer.toString(unitID));
            }
        }   

        return sb.toString().stripTrailing();
    }

    private int countUnitsAtStation(int stationId) {
        int count = 0;
        for (int i = 0; i < unitCount; i++) {
            Unit u = units[i];
            if (u != null && u.getHomeStationId() == stationId) count++;
        }
        return count;
    }

    public Unit getUnitFromId(int unitId){
        for(int j = 0; j < MAX_UNITS; j++){
            if (units[j] != null && units[j].getUnitId() == unitId)
                return units[j];
        }
        throw new RuntimeException("invalid unit ID");
    }


    public Station getStationFromId(int stationId){
        for (int i = 0; i < MAX_STATIONS; i++){
            if (stations[i] != null && stations[i].getStationId() == stationId)
                return stations[i];
        }
        throw new RuntimeException("invalid station ID");
    }

    public Incident getIncidentFromId(int incidentId){
        for(int i = 0; i < MAX_INCIDENTS; i++){
            if (incidents[i] != null && incidents[i].getIncidentId() == incidentId)
                return incidents[i];
        }
        throw new RuntimeException("invalid Incident ID " + Integer.toString(incidentId));
    }

    public boolean validLocation(int x, int y){
        int[] size = getGridSize();
        return !(x < 0 || y < 0 || x > size[0] - 1 || y > size[1] - 1);
    }

    public Unit[] getERUnits(){
        int[] unitIDs = getUnitIds();
        Unit[] units = new Unit[unitIDs.length];

        int unitCount = 0;
        for (int i = 0; i < unitIDs.length; i++){
            Unit unit;
            try {
                unit = getUnitFromId(unitIDs[i]);
            }
            catch(Exception e){
                System.out.println("Invalid unit ID: " + e.toString());
                continue;
            }

            if (unit.getStatus() == UnitStatus.EN_ROUTE){
                units[unitCount] = unit;
                unitCount ++;
            }
        }

        Unit[] resized = new Unit[unitCount];
        System.arraycopy(units, 0, resized, 0, unitCount);
        return resized;
    }

    public Unit[] getAvailableUnits(IncidentType type){
        //correct type, no assugned incident, not out of service
        int[] unitIDs = getUnitIds();
        Unit[] units = new Unit[unitIDs.length];

        UnitType unitType = (type == IncidentType.FIRE) ? UnitType.FIRE_ENGINE : 
            (type == IncidentType.CRIME) ? UnitType.POLICE_CAR : UnitType.AMBULANCE;

        int unitCount = 0;
        for (int i = 0; i < unitIDs.length; i++){
            Unit unit;
            try{
                unit = getUnitFromId(unitIDs[i]);
            }
            catch(Exception e){
                System.out.println("Invalid ID: " + e.toString());
                continue;
            }
            if (unit.getAssignedIncidentId() == -1 && unit.getUnitType() == unitType && unit.getStatus() != UnitStatus.OUT_OF_SERVICE){
                units[unitCount] = unit;
                unitCount ++;
            }
        }

        Unit[] resized = new Unit[unitCount];
        System.arraycopy(units, 0, resized, 0, unitCount);
        return resized;
    }


    public boolean isUnitIdLower(Unit u1, Unit u2){
        return u1.getUnitId() < u2.getUnitId() || isStationIdLower(u1, u2);
    }

    public boolean isStationIdLower(Unit u1, Unit u2){
        return u1.getUnitId() == u2.getUnitId() && u1.getHomeStationId() < u2.getHomeStationId();
    }

    public Incident[] getIncidents(){
        int[] incidentIDs = getIncidentIds();
        Incident[] incidents = new Incident[incidentIDs.length];

        for(int i = 0; i < incidentIDs.length; i++){
            Incident incident;
            try{
                incident = getIncidentFromId(incidentIDs[i]);
            }
            catch(Exception e){
                System.out.println("invalid incident ID: " + e.toString());
                continue;
            }
            incidents[i] = incident;
        }

        return incidents;
    }

    public void moveUnit(Unit unit){
        Incident incident;
        try{
            incident = getIncidentFromId(unit.getAssignedIncidentId());
        }
        catch(Exception e){
            System.out.println("invalid incident id: " + e.toString());
            return;
        }
            //add moving logic in order N E S W
        int dist = unit.manhattenDist(unit.getLocation(), incident.getLocation());

        //1.1: list all options removing blocked or out of bounds
        int[][] dir = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; //NESW

        //1.2: take the first legal move that reduces manhatten distance
        for (int[] d : dir) {

            int newX = unit.getX() + d[0];
            int newY = unit.getY() + d[1];
            try {
                if (!validLocation(newX, newY) || map.isBlocked(newX, newY))
                    continue;
            }
            catch(Exception e){
                System.out.println("out of bounds: " + e.toString());
                continue;
            }
            int newDistance = unit.manhattenDist(new int[]{newX, newY}, incident.getLocation());
            if (newDistance < dist){
                unit.moveUnit(d);
                return;
            }
        }            

        //1.3: if none reduce distance, take first legal in order N E S W

        for (int[] d : dir){
            int newX = unit.getX() + d[0];
            int newY = unit.getY() + d[1];
            try {
                if (!validLocation(newX, newY) || map.isBlocked(newX, newY))
                    continue;
            }
            catch(Exception e){
                System.out.println("Out of bounds direction");
                continue;
            }
            unit.moveUnit(d);
            return;
        }

        //1.4: if no move available, stay put
        return;
    }
}
