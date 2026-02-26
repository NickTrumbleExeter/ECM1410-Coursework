package cityrescue;

public class Station {

    private final int stationId;
    private final String name;
    private final int x;
    private final int y;
    private int capacity;
    
    public static int stationCount = 0;

    Unit[] units;

    public Station(int stationId, String name, int x, int y, int capacity) {
        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = capacity;

        this.units = new Unit[capacity];
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

    public void setCapacity(int newCapacity) {
        this.capacity = newCapacity;
    }

}
