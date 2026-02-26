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

    public void addObstacle(int x, int y){
        try{
            blocked[x][y] = true;
        }
        catch (InvalidLocationException e) {
            throw new InvalidLocationException("Location is out of bounds ", e);
        }
    }

    public void removeObstacle(int x, int y){
        try{
            blocked[x][y] = false;
        }
        catch (InvalidLocationException e){
            throw new InvalidLocationException("Location is out of bounds ", e);
        }
    }
}
