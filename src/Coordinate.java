/**
 * used to store list of positions a user picks to play 
 * 
 * a position that a player picks to play is represesnted
 * as Coordinate object
 *
 */
public class Coordinate {
    private int row;
    private int col;
    
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            Coordinate c = (Coordinate) o;
            return this.row == c.getRow() && this.col == c.getCol();
        } else {
            return false;
        }
    }

    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }
    
    public String toString() {       
        return  row + "/" + col;
    }
    
}
