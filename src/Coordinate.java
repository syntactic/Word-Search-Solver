
/**
 * Coordinate class that represents row x column 
 * coordinates of a letter in a grid where valid 
 * grid coordinates are positive and are unbounded.
 * 
 * @author Timothy Ho
 * @version $Date: 2011/9/27 2:49:25 $
 *
 */
public class Coordinate {

	private int row;
	private int col;
	
	/**
	 * Constructor for Coordinate class
	 */
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	
	/**
	 * Default constructor for Coordinate class
	 */
	public Coordinate () {
		row = -1;
		col = -1;
	}
	
	
	/**
	 * Getter for row variable
	 * 
	 * @return row coordinate
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter for col variable
	 * 
	 * @return column coordinate
	 */
	public int getCol() {
		return col;
	}
	
	
	/**
	 * Setter for row variable
	 * 
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	
	/**
	 * Setter for column variable
	 * 
	 * @param col
	 */
	public void setCol(int col) {
		this.col = col;
	}
	
	
	/**
	 * Checks equality of two coordinates by seeing if
	 * their row and col fields are equal
	 *
	 * @param coord2 the coordinate being compared to
	 * @return true, if coordinates are equal
	 */
	public boolean equals(Coordinate coord2) {
		return (row==coord2.getRow() && col == coord2.getCol());
	}	
}
