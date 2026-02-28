package cityrescue;

import cityrescue.exceptions.InvalidLocationException;

public class CityMap {
    private final int gridWidth;
    private final int gridHeight;
    boolean[][] blocked;

    public CityMap(int gridWidth, int gridHeight){
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        blocked = new boolean[gridWidth][gridHeight];
    }

    public int getGridWidth(){
        return gridWidth;
    }

    public int getGridHeight(){
        return gridHeight;
    }

    //method for validating a coordinate 
    // x and y must be < and not =< width and height as these are always the number of valid coordinates +1
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }

    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (!inBounds(x, y)) throw new InvalidLocationException("Location out of bounds");
        blocked[x][y] = true;
    }

    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (!inBounds(x, y)) throw new InvalidLocationException("Location out of bounds");
        blocked[x][y] = false;
    }

    public boolean isBlocked(int x, int y) throws InvalidLocationException {
        if (!inBounds(x, y)) throw new InvalidLocationException("Location out of bounds");
        return blocked[x][y];
    }
    
}
