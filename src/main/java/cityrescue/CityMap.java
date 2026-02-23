package cityrescue;

public class CityMap {
    private final int gridSize;
    boolean[][] obstacleMap;

    public CityMap(int gridSize){
        this.gridSize = gridSize;
        obstacleMap = new boolean[gridSize][gridSize];
    }

    public int getGridSize(){
        return gridSize;
    }
}
