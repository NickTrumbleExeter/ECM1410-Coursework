package cityrescue;

import cityrescue.exceptions.InvalidCapacityException;

public class Station {
    private static final int MAX_UNITS = 50;
    private final int stationId;
    private final String name;
    private final int x;
    private final int y;
    private int capacity;
    private int unitCount;
    
    public static int stationCount = 0;
    public static int IdCount = 0;
    Unit[] units;

    public Station(int stationId, String name, int x, int y, int capacity) {
        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        this.units = new Unit[capacity];
        IdCount++;
        stationCount++;
        unitCount = 0;
    }

    public int getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStationCount(){
        return stationCount;
    }

    public static int getMaxUnits(){
        return MAX_UNITS;
    }
    
    public int getUnitCount(){
        return unitCount;
    }

    public void setCapacity(int newCapacity) throws InvalidCapacityException{
        if (newCapacity < 0 || unitCount > newCapacity)
            throw new InvalidCapacityException();
        this.capacity = newCapacity;
    }

}
