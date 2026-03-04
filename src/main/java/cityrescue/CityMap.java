package cityrescue;

import cityrescue.exceptions.InvalidLocationException;

public class CityMap {
    private final int gridWidth;
    private final int gridHeight;
    private static int obstacleCount = 0;
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

    /**
     * method for checking if a coordinate xy is in bounds or not.
     * both x and y must not be less than 0 and muxt be less than the grid width and height
     * 
     * @param x the x coordinate being checked 
     * @param y the y coordinate being checked 
     * 
     * @return returns a boolean for if the coordinate is valid or not 
     */
    //method for validating a coordinate 
    // x and y must be < and not =< width and height as these are always the number of valid coordinates +1
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }

    /**
     * adds an obsticle to a coordinate xy.
     * sets that coodinate to blocked 
     * increments obstible count 
     * 
     * @param x x coordinate for the added obsticle 
     * @param y y coordinate of the added obsticle 
     * @throws InvalidLocationException if the location selected is out of bounds 
     */
    //add check so an obsticle cant be placed on a station? 
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (!inBounds(x, y)) throw new InvalidLocationException("Location out of bounds");
        blocked[x][y] = true;
        obstacleCount++;
    }

    /**
     * removes an obsticle from the map at a given xy 
     * removes blocked status from that coordinate 
     * 
     * @param x x coordinate of the obsticle 
     * @param y y coordinate of the obsticle 
     * @throws InvalidLocationException if the coordinates entered are out of bounds 
     */
    //add check so only coordinates where blocked = true can be removed 
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (!inBounds(x, y)) throw new InvalidLocationException("Location out of bounds");
        blocked[x][y] = false;
        obstacleCount--;
    }

    /**
     * checks is a given coordinate is blocked 
     * 
     * @param x x coordinate to be checked 
     * @param y y coordinate to be checked 
     * @return returns a boolean depending on if the coordinate is blocked or not 
     * @throws InvalidLocationException if the coordinates entered are out of bounds 
     */
    public boolean isBlocked(int x, int y) throws InvalidLocationException {
        if (!inBounds(x, y)) throw new InvalidLocationException("Location out of bounds");
        return blocked[x][y];
    }

    /**
     * method to return the total number of obsticles on the grid 
     * @return returns an int of the number of obsticles 
     */
    public static int getObstacleCount(){
        return obstacleCount;
    }
    
}
