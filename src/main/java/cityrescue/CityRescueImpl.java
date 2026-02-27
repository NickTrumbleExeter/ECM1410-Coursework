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
    // add the exceptions in the methods
    private final int MAX_STATIONS = 20;
    private final int MAX_INCIDENTS = 200;

    private int tick = 0;
    private CityMap map;
    private Station[] stations = new Station[MAX_STATIONS];
    private Incident[] incidents = new Incident[MAX_INCIDENTS];

    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        // TODO: implement
        map = new CityMap(width, height);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getGridSize() {
        return new int[]{map.getGridWidth(), map.getGridHeight()};
    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        map.addObstacle(x, y);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        map.removeObstacle(x, y);
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        // TODO: implement
        if (name.isBlank())
            throw new InvalidNameException();

        int[] size = getGridSize();
        if (x < 0 || y < 0 || x > size[0] - 1 || y > size[1] - 1)
            throw new InvalidLocationException();
        
        Station station = new Station(Station.IdCount, name, x, y, Station.getMaxUnits());
        return Station.IdCount - 1;
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        // TODO: implement
        Station station = getStationFromId(stationId);
        station.setCapacity(maxUnits);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getStationIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getUnitIds() {
        // TODO: implement
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
        return resized;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        // TODO: implement
        Unit unit = geUnitFromId(unitId);
        String formatted = String.format("U#%d TYPE=%s HOME=%s LOC=(%d,%d) STATUS=%s INCIDENT=%d WORK=%d", 
            unitId, unit.getUnitType());//impliment the rest of the features
        return formatted;
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        // TODO: implement
        Incident incident = new Incident(type, severity, x, y);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        Incident incident = getIncidentFromId(incidentId);
        incident.updateStatus(IncidentStatus.CANCELLED);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        // TODO: implement
        Incident incident = getIncidentFromId(incidentId);
        incident.updateSeverity(newSeverity);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getIncidentIds() {
        // TODO: implement
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
        return resized;
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        // TODO: implement
        Incident incident = getIncidentFromId(incidentId);
        String formatted = String.format("I#%d TYPE=%s SEV=%s LOC=(%d,%d) STATUS=%s UNIT=%d", 
            incidentId, incident.getIncidentType(), incident.getIncidentSeverity(), incident.getLocation(),
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
        // TODO: implement
        tick++;
        //add the rest of the method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getStatus() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public Unit geUnitFromId(int unitId){
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
}
