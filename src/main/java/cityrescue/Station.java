package cityrescue;

import cityrescue.exceptions.InvalidCapacityException;

public class Station {
    private final int stationId;
    private final String name;
    private final int x;
    private final int y;
    private int capacity;
    private int unitCount;

    Unit[] units;

    public Station(int stationId, String name, int x, int y, int capacity) {
        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        this.units = new Unit[capacity];
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
    
    public int getUnitCount(){
        return unitCount;
    }

    public void setCapacity(int newCapacity) throws InvalidCapacityException{
        if (newCapacity <= 0) throw new InvalidCapacityException("Capacity must be positive");
        this.capacity = newCapacity;
    }

}
