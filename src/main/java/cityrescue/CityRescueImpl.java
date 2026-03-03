package cityrescue;

import java.util.Arrays;
import cityrescue.*;//importing all classes
import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {

    // TODO: add fields (map, arrays for stations/units/incidents, counters, tick, etc.)
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

    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if (width < 0 || height < 0)
            throw new InvalidGridException("Invalid Height for initialisation");

        tick = 0;
        stations = new Station[MAX_STATIONS];
        incidents = new Incident[MAX_INCIDENTS];

        map = new CityMap(width, height);
    }

    @Override
    public int[] getGridSize() {
        return new int[]{map.getGridWidth(), map.getGridHeight()};
    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (!validLocation(x, y))
            throw new InvalidLocationException("invalid obstacle location (addObstacle)");

        map.addObstacle(x, y);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (!validLocation(x, y))
            throw new InvalidLocationException("invalid obstacle location (removeObstacle)");

        map.removeObstacle(x, y);
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        //defragmentation?
        if (name.isBlank())
            throw new InvalidNameException("Name cannot be blank");

        //int[] size = getGridSize();
        if (!map.inBounds(x, y) || map.isBlocked(x, y))
            throw new InvalidLocationException("Location is out of bounds or blocked");

        int id = nextStationId++;
        
        stations[stationCount++] = new Station(id, name, x, y, DEFAULT_STATION_CAPACITY);
        return id;
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        Station station = getStationFromId(stationId);

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

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        Station station = getStationFromId(stationId);
        station.setCapacity(maxUnits);
    }

    @Override
    public int[] getStationIds() {
        int[] stationIds = new int[stationCount];
        for (int i = 0; i < stationCount; i++){
                stationIds[i] = stations[i].getStationId();
            }
            Arrays.sort(stationIds);
            return stationIds;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        Station station = getStationFromId(stationId);

        if (type == null)
            throw new IllegalStateException("Unit type is null");
        if (unitCount >= MAX_UNITS)
            throw new IllegalStateException("Max units reached");
        if(countUnitsAtStation(stationId) >= station.getCapacity())
            throw new IllegalStateException("The station is at max capacity");
            
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

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = getUnitFromId(unitId);
        Station station = getStationFromId(unit.getHomeStationId());
        if (unit.getStatus() == UnitStatus.EN_ROUTE || unit.getStatus() == UnitStatus.AT_SCENE)
            throw new IllegalStateException("illegal status");

        for (int i = 0; i < MAX_UNITS; i++){
            if (units[i].getUnitId() == unitId){
                units[i] = null;
                break;
            }
        }
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = getUnitFromId(unitId);

        if (unit.getStatus() != UnitStatus.IDLE)
            throw new IllegalStateException("invalid status");


            unit.setHomeStationId(newStationId);
        //if no space in new station
        throw new IllegalStateException("max units reached");
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = getUnitFromId(unitId);
        //check this
        if (outOfService){
            if (unit.getStatus() != UnitStatus.IDLE)
                throw new IllegalStateException("invalid state");
            unit.setStatus(UnitStatus.OUT_OF_SERVICE);
        }
        else {
            unit.setStatus(UnitStatus.IDLE);
        }
    }

    @Override
    public int[] getUnitIds() {
        int[] unitIds = new int[MAX_UNITS];
        int counter = 0;
        for (int j = 0; j < MAX_UNITS; j++){
            if (units[j] != null){
                unitIds[counter] = units[j].getUnitId();
                counter++;
            }
        }
        
        int[] resized = new int[counter];
        System.arraycopy(unitIds, 0, resized, 0, counter);
        Arrays.sort(resized);
        return resized;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        Unit unit = getUnitFromId(unitId);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("U#%d", unitId));
        sb.append(String.format(" TYPE=%s", unit.getUnitType()));
        sb.append(String.format(" HOME=%s", unit.getHomeStationId()));
        int[] location = getStationFromId(unit.getHomeStationId()).getLocation();
        sb.append(String.format(" LOC=(%d,%d)", location[0], location[1]));
        sb.append(String.format(" STATUS=%s", unit.getStatus()));
        sb.append(String.format(" INCIDENT=%d", unit.getAssignedIncidentId()));
        
        int work = getIncidentFromId(unit.getAssignedIncidentId()).getTicksRemaining();
        sb.append(String.format(" WORK=%d", work));
        return sb.toString();
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        if (!validLocation(x, y) || map.blocked[x][y])
            throw new InvalidLocationException("Not a valid position");

        if (type == null || severity < 1 || severity > 5)
            throw new InvalidSeverityException("not a valid severity");

        Incident incident = new Incident(nextIncidentId++, type, severity, x, y);
        //incremented when new incident created
        for (int i = 0; i < MAX_INCIDENTS; i++){
            if (incidents[i] == null){
                incidents[i] = incident;
                break;
            }
        }
        return incident.getIncidentId();
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        Incident incident = getIncidentFromId(incidentId);
        switch(incident.getIncidentStatus()){
            case REPORTED:
                incident.updateStatus(IncidentStatus.CANCELLED);
                break;
            case DISPATCHED:
                //add this !!
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        Incident incident = getIncidentFromId(incidentId);

        if (newSeverity < 1 || newSeverity > 5)
            throw new InvalidSeverityException("Invalid severity");

        if (incident.getIncidentStatus() == IncidentStatus.RESOLVED || incident.getIncidentStatus() == IncidentStatus.CANCELLED)
            throw new IllegalStateException("invalid state");

        incident.updateSeverity(newSeverity);
    }

    @Override
    public int[] getIncidentIds() {
        int[] incidentIds = new int[MAX_INCIDENTS];
        int counter = 0;
        for (int i = 0; i < MAX_INCIDENTS; i++){
            if (incidents[i] != null){
                incidentIds[counter] = incidents[i].getIncidentId();
                counter++;
            }
        }

        int[] resized = new int[counter];
        System.arraycopy(incidentIds, 0, resized, 0, counter);
        Arrays.sort(resized);
        return resized;
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        Incident incident = getIncidentFromId(incidentId);
        String formatted = String.format("I#%d TYPE=%s SEV=%s LOC=(%d,%d) STATUS=%s UNIT=%d", 
            incidentId, incident.getType(), incident.getIncidentSeverity(), incident.getX(),
            incident.getY(), incident.getIncidentStatus(), incident.getAssignedUnit());
        return formatted;
    }

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
            }
        }
    }

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
            if (unit.getLocation() == getIncidentFromId(unit.getAssignedIncidentId()).getLocation()){
                unitAtScenes[arrivedCount] = getIncidentFromId(unit.getAssignedIncidentId());
            }
        }

        
        //3: process on scene work (resolveTick())
        for(Incident incident : unitAtScenes){
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
            sb.append(String.format("I#%d TYPE=%s SEV=%d LOC=(%d,%d) STATUS=%s UNIT=%d\n", 
                incidentID, incident.getType(), incident.getIncidentSeverity(), incident.getX(), 
                incident.getY(), incident.getIncidentStatus(), unit));
        }        

        sb.append("UNITS\n");
        for (int unitID : units){
            Unit unit = getUnitFromId(unitID);
            String incident = (unit.getAssignedIncidentId() > 0) ? Integer.toString(unit.getAssignedIncidentId()) : "-";
            sb.append(String.format("I#%d TYPE=%s HOME=%d LOC=(%d,%d) STATUS=%s INCIDENT=%d\n", 
                unitID, unit.getUnitType(), unit.getHomeStationId(), unit.getX(), 
                unit.getY(), unit.getStatus(), incident));
        }   

        return sb.toString();
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
            if (incidents[i] != null && incidents[i].getIncidentId() == incidentId){
                return incidents[i];
            }
        }
        throw new RuntimeException("invalid Incident ID");
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
        return u1.getUnitId() == u2.getUnitId() && u1.getUnitId() < u2.getUnitId() || isStationIdLower(u1, u2);
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
