package cityrescue;

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
    // update the constructors for units to include IDs
    private final int MAX_STATIONS = 20;
    private final int MAX_INCIDENTS = 200;

    private int tick = 0;
    private CityMap map;
    private Station[] stations = new Station[MAX_STATIONS];
    private Incident[] incidents = new Incident[MAX_INCIDENTS];

    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if (width < 0 || height < 0)
            throw new InvalidGridException();

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
            throw new InvalidLocationException();

        map.addObstacle(x, y);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (!validLocation(x, y))
            throw new InvalidLocationException();

        map.removeObstacle(x, y);
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        //defragmentation?
        if (name.isBlank())
            throw new InvalidNameException();

        int[] size = getGridSize();
        if (!validLocation(x, y) || map.blocked[x][y])
            throw new InvalidLocationException();
        
        Station station = new Station(Station.IdCount, name, x, y, Station.getMaxUnits());
        stations[Station.stationCount] = station;
        return Station.IdCount - 1;
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        Station station = getStationFromId(stationId);

        if (station.getUnitCount() != 0)
            throw new IllegalStateException();

        for (int i = 0; i < MAX_STATIONS; i++){
            if (stations[i].getStationId() == stationId)
                stations[i] = null;
        }

        Station.stationCount--;
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        Station station = getStationFromId(stationId);
        station.setCapacity(maxUnits);
    }

    @Override
    public int[] getStationIds() {
        int[] stationIds = new int[MAX_STATIONS];
        int counter = 0;
        for (int i = 0; i < MAX_STATIONS; i++){
            if (stations[i] != null){
                stationIds[counter] = stations[i].getStationId();
                coutner++;
            }
        }
        
        int[] resized = new int[counter];
        System.arraycopy(stationIds, 0, resized, 0, counter);
        return Arrays.sort(resized);
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        Station station = getStationFromId(stationId);

        if (type == null)
            throw new IllegalStateException();

        Unit unit = new Unit(type, stationId);

        for (int i = 0; i < Station.getMaxUnits; i++){
            if (station.units[i] == null){
                station.units[i] = unit;
                return unit.getUnitId();
            }
        }
        throw new InvalidUnitException();
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = geUnitFromId(unitId);
        Station station = getStationFromId(unit.getStationId());
        if (unit.getStatus() == UnitStatus.EN_ROUTE || unit.getStatus() == UnitStatus.AT_SCENE)
            throw new IllegalStateException();

        for (int i = 0; i < Station.getMaxUnits(); i++){
            if (station.units[i].getUnitId() == unitId){
                station.units[i] = null;
                break;
            }
        }
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = geUnitFromId(unitId);
        Station station = getStationIds(unit.getStationId());
        Station newStation = getStationFromId(newStationId);

        if (unit.getStatus() != UnitStatus.IDLE)
            throw new IllegalStateException();

        for (int i = 0; i < Station.getMaxUnits(); i++) {
            if (station.units[i].getUnitId() == unitId)
                station.units[i] = null;
        }

        for (int i = 0; i < Station.getMaxUnits(); i++){
            if (newStation.units[i] == null) {
                newStation.units[i] = unit;
                unit.updateStationId(newStationId);
                return;
            }
        }
        //if no space in new station
        throw new InvalidCapacityException();
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = geUnitFromId(unitId);

        //check this
        if (outOfService){
            if (unit.getStatus() != UnitStatus.IDLE)
                throw new IllegalStateException();
            unit.updateStatus(UnitStatus.OUT_OF_SERVICE);
        }
        else {
            unit.updateStatus(UnitStatus.IDLE);
        }
    }

    @Override
    public int[] getUnitIds() {
        int[] unitIds = new int[MAX_STATIONS * Station.getMaxUnits()];
        int counter = 0;
        for (int i = 0; i < MAX_STATIONS; i++){
            for (int j = 0; j < Station.getMaxUnits(); j++){
                if (stations[i] != null && stations[i].units[j] != null){
                    unitIds[counter] = stations[i].units[j].getUnitId();
                    counter++;
                }
            }
        }
        int[] resized = new int[counter];
        System.arraycopy(unitIds, 0, resized, 0, counter);
        return Arrays.sort(resized);
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        // TODO: implement
        Unit unit = geUnitFromId(unitId);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("U#%d", unidId));
        sb.append(String.format(" TYPE=%s", unit.getUnitType()));
        sb.append(String.format(" HOME=%s", unit.getStationId()));
        int[] location = getStationFromId(unit.getStationId()).Location;
        sb.append(String.format(" LOC=(%d,%d)", location[0], location[1]));
        sb.append(String.format(" STATUS=%s", unit.getStatus()));

        //add these
        int incidentId;
        sb.append(String.format(" INCIDENT=%d", incidentId));

        int work;
        sb.append(String.format(" WORK=%d", work));
        return sb.toString();
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        int[] size = getGridSize();
        if (!validLocation(x, y) || map.blocked[x][y])
            throw new InvalidLocationException();

        if (type == null || severity < 1 || severity > 5)
            throw new InvalidSeverityException();

        Incident incident = new Incident(type, severity, x, y);
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
            case IncidentStatus.REPORTED:
                incident.updateStatus(IncidentStatus.CANCELLED);
                break;
            case IncidentStatus.DISPATCHED:
                //add this !!
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        Incident incident = getIncidentFromId(incidentId);

        if (severity < 1 || severity > 5)
            throw new InvalidSeverityException();

        if (incident.getIncidentStatus() == IncidentStatus.RESOLVED || incident.getIncidentStatus() == IncidentStatus.CANCELLED)
            throw new IllegalStateException();

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
        return Arrays.sort(resized);
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        // TODO: implement
        Incident incident = getIncidentFromId(incidentId);
        String formatted = String.format("I#%d TYPE=%s SEV=%s LOC=(%d,%d) STATUS=%s UNIT=%d", 
            incidentId, incident.getType(), incident.getIncidentSeverity(), incident.getLocation(),
            incident.getIncidentStatus(), incident.getAssignedUnit());
        return formatted;
    }

    @Override
    public void dispatch() {
        // TODO: implement

        throw new UnsupportedOperationException("Not implemented yet");
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
            if (unit.getLocation() == incident.getLocation()){
                unitAtScenes[arrivedCount] = incident;
            }
        }

        
        //3: process on scene work (resolveTick())
        for(Incident incident : unitAtScenes){
            incident.resolveTick();
        }

        //4: resolve completed incidents in ascending incident id
        //maybe remove incident once completed?
        for (Incident incident : incidents){
            if (incident.getTicksToResolve() == 0){
                Unit unitAtScene = geUnitFromId(incident.getAssignedUnit());
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
            stations.length, units.length, incidents.length, city.getObstacleCount()));
        
        sb.append("INCIDENTS\n");
        for (int incidentID : incidents){
            Incdient incident = getIncidentFromId(incidentID);
            string unit = (incident.getAssignedUnit() > 0) ? incident.getAssignedUnit() : "-";
            sb.append(String.format("I#%d TYPE=%s SEV=%d LOC=(%d,%d) STATUS=%s UNIT=%d\n", 
                incidentID, incident.getType(), incident.getIncidentSeverity(), incident.getX(), 
                incident.getY(), incident.getStatus(), unit));
        }        

        sb.append("UNITS\n");
        for (int unitID : units){
            Unit unit = geUnitFromId(unitID);
            string incident = (unit.getAssignedIncidentId() > 0) ? incident.getAssignedIncidentId() : "-";
            sb.append(String.format("I#%d TYPE=%s HOME=%d LOC=(%d,%d) STATUS=%s INCIDENT=%d\n", 
                unitID, unit.getUnitType(), unit.getHomeStationId(), unit.getX(), 
                unit.getY(), unit.getStatus(), incident));
        }   

        return sb.toString();
    }

    public Unit geUnitFromId(int unitId) throws IDNotRecognisedException{
        for (int i = 0; i < MAX_STATIONS; i++){
            for(int j = 0; j < stations[i].getCapacity(); j++){
                if (stations[i].units[j] != null && stations[i].units[j].getUnitId() == unitId)
                    return stations[i].units[j];
            }
        }
        throw new IDNotRecognisedException();
    }

    public Station getStationFromId(int stationId){
        for (int i = 0; i < MAX_STATIONS; i++){
            if (stations[i] != null && stations[i].getStationId() == stationId)
                return stations[i];
        }
        throw new IDNotRecognisedException();
    }

    public Incident getIncidentFromId(int incidentId){
        for(int i = 0; i < MAX_INCIDENTS; i++){
            if (incidents[i] != null && incidents[i].getIncidentId() == incidentId){
                return incidents[i];
            }
        }
        throw new IDNotRecognisedException();
    }

    public boolean validLocation(int x, int y){
        return !(x < 0 || y < 0 || x > size[0] - 1 || y > size[1] - 1);
    }

    public Unit[] getERUnits(){
        int[] unitIDs = getUnitIds();
        Unit[] units = new Unit[unitIDs.length];

        int unitCount = 0;
        for (int i = 0; i < unitIDs.length; i++){
            if (getUnitFromId(unitIds[i]).getStatus() == UnitStatus.EN_ROUTE){
                units[unitCount] = geUnitFromId(unitIds[i]);
                unitCount ++;
            }
        }

        Unit[] resized = new Unit[unitCount];
        System.arraycopy(units, 0, resized, 0, unitCount);
        return resized;
    }

    public Incident[] getIncidents(){
        int[] incidentIDs = getIncidentIds();
        Incident[] incidents = new Incident[incidentIDs.length];

        for(int i = 0; i < incidentIDs.length; i++){
            incidents[i] = getIncidentFromId(incidentIDs[i]);
        }

        return incidents;
    }

    public void moveUnit(Unit unit){
        Incident incident = getIncidentFromId(unit.getAssignedIncidentId());
            //add moving logic in order N E S W
            int dist = unit.manhattenDist(unit.getLocation(), incident.getIncidentId());

            //1.1: list all options removing blocked or out of bounds
            int[][] dir = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; //NESW

            //1.2: take the first legal move that reduces manhatten distance
            for (int[] d : dir) {

                int newX = unit.getX() + d[0];
                int newY = unit.getY() + d[1];

                if (!validLocation(newX, newY) || map.isBlocked(newX, newY))
                    continue;

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

                if (!validLocation(newX, newY) || map.isBlocked(newX, newY))
                    continue;

                unit.moveUnit(d);
                return;
            }

            //1.4: if no move available, stay put
            return;
    }
}
