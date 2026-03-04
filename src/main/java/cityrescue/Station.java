package cityrescue;

/**
 * station class 
 * handles data and methods for station objects 
 * includes:
 * -ID
 * -name 
 * -coordinates 
 * -capacity 
 */
public class Station {
    private final int stationId;
    private final String name;
    private final int x;
    private final int y;
    private int capacity;

    public Station(int stationId, String name, int x, int y, int capacity) {
        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
    }

    /**
     * returns teh ID of a specified station 
     * 
     * @return teh int ID of the station 
     */
    public int getStationId() {
        return stationId;
    }

    /**
     * returns the name of the station 
     * 
     * @return string of teh stations name 
     */
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * returns the capacity of the station 
     * 
     * @returnreturns the int capacity of the station 
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * sets the new capacity for the station 
     * @param newCapacity new capacity for the station 
     */
    public void setCapacity(int newCapacity){
        this.capacity = newCapacity;
    }

}
