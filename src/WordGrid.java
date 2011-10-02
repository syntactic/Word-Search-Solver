import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

/**
 * 
 * WordGrid class that encompasses most functionality 
 * of the word search. Builds the grid, and contains 
 * methods for grid traversal and word searching.
 * 
 * @author Timothy
 * @version $Date: 2011/9/27 2:49:25 $
 *
 */

public class WordGrid {
	
	/** 2 dimensional array representing the word grid */
	private char[][] grid; 
	
	/** number of rows */
	private int n;
	
	/** number of columns */
	private int m;
	
	/** boolean flag for wraparound */
	private boolean wrap;
	
	/** variable that keeps track of current row when building grid */
	private int currRow = 0;
	
	/** multi value hash map that uses characters as keys
	 * and coordinates as values to represent grid
	 */
	private HashMap<Character,ArrayList<Coordinate>> hashGrid;
	
	/** Coordinate variable that is compared to every traversed 
	 * coordinate if wrap is allowed, to make sure that the same 
	 * coordinate is not traversed twice (denoting a loop)
	 */
	private Coordinate currentStart;
	
	/** constants representing numerical stepping through the 
	 * grid, with negative denoting either upwards or leftwards, 
	 * and positive denoting either downwards or rightwards
	 */
	private static final int DECREMENT = -1;
	private static final int INCREMENT = 1;
	
	/**
	 * Constructor for grid, creates both 2 dimensional 
	 * array and HashMap
	 * 
	 * @param num1 Number of rows
	 * @param num2 Number of columns
	 */
	public WordGrid(int num1, int num2) {
		n = num1;
		m = num2;
		grid = new char[n][m];
		hashGrid = new HashMap<Character,ArrayList<Coordinate>>();
	}

	
	/**
	 * Setter for grid's wrap flag
	 * 
	 * @param bool 
	 * @throws Exception 
	 */
	public void setWrap(String wrap) throws Exception {
		if (wrap.equalsIgnoreCase("WRAP")) {
			this.wrap = true;
		} else if (wrap.equalsIgnoreCase("NO_WRAP")) {
			this.wrap = false;
		} else {
			throw new Exception("Invalid wrap parameter.");
		}
	}
	
	
	/**
	 * Builds the word grid line by line, inserting
	 * characters into both the grid multidimensional 
	 * array and the hash map
	 * 
	 * @param str Row of characters to be added to the grid
	 * @throws Exception if the string isn't the right length
	 */
	public void addLine(String str) throws Exception {
		if (str.length() == 0) { 
			throw new Exception("Found blank line in word grid!"); 
		} else if (str.length() > 0 && str.length()!=m) {
			throw new Exception("Word search not " + n + " x " + m);
		} else if(!str.matches("^[a-zA-Z]+$")) {
			throw new Exception("Found nonletter in grid!");
		}
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			grid[currRow][i] = c;
			if (hashGrid.containsKey(c)) {
				hashGrid.get(c).add(new Coordinate(currRow, i));
			} else {
				ArrayList<Coordinate> positions = new ArrayList<Coordinate>();
				positions.add(new Coordinate(currRow, i));
				hashGrid.put(c, positions);
			}
		}
		currRow++;
	}
	

	/**
	 * Given a word, gets the first character of the word,
	 * and checks the hash map for that key to find its 
	 * location in the grid. After getting its adjacent tiles, 
	 * this method pursues each valid direction to see if the 
	 * word is found. If so, this prints the starting and
	 * ending coordinates of the word, if not, it prints 
	 * "NOT FOUND".
	 * 
	 * @param word String to be found in the grid
	 * @throws Exception if word is empty
	 */
	public void findWord(String word) throws Exception {
		if (word.length() == 0) { throw new Exception("Found blank line in word list."); }
		if (!wrap && word.length() > Math.max(n, m)) {
			// if the word search doesn't allow wrap, don't bother searching for words longer than any dimension
			System.out.println("NOT FOUND");
			return;
		}
		ArrayList<Coordinate> positions = hashGrid.get(word.charAt(0));
		if (positions!=null) {
			for (Coordinate c : positions) { // gets all locations of the first character
				currentStart = c;
				ArrayList<Coordinate> adjacents = getAdjacents(c);
				for (Coordinate nextLetter : adjacents) {
					if (nextLetter!=null) {				
						String relation = getRelation(c, nextLetter);
						Coordinate endOfWord = pursueDirection(nextLetter, relation, word.substring(1));
						if (endOfWord!=null) {
							System.out.printf("(%d, %d) (%d, %d)\n", c.getRow(), c.getCol(), endOfWord.getRow(), endOfWord.getCol());
							return;
						}
					}
				}
			}
		}
		System.out.println("NOT FOUND");
		return;
	}
	
	
	/**
	 * Given the coordinates of a letter and a direction to follow,
	 * this method continues in the direction to see if the word 
	 * being searched for is found in that direction. If it is, the 
	 * coordinates of its last letter are returned. If not, this 
	 * returns null.
	 * 
	 * @param letter Coordinate of current letter being examined
	 * @param relation Relative position of current letter to previous letter
	 * @param restOfWord Substring of original word being search for, 
	 * 					 containing the rest of the word that needs to be found
	 * @return
	 */
	public Coordinate pursueDirection(Coordinate letter, String relation, String restOfWord) {
		if (wrap && currentStart.equals(letter)) { return null; } // checks if we've reused a letter
		while(!restOfWord.isEmpty()) {
			int currentRow = letter.getRow();
			int currentCol = letter.getCol();
			if (grid[currentRow][currentCol]==restOfWord.charAt(0)) { // checks if letter at grid matches substring's first letter
				if (restOfWord.length()==1) { // checks to see if we found the last letter in the word
					return letter;
				}
				Coordinate nextLetter = followDirection(letter, relation);
				if (nextLetter!=null) {
					return pursueDirection(nextLetter, relation, restOfWord.substring(1));
				} else {
					return null;
				}
			} else {
				return null;
			}	
		}
		return null;
	}
	
	
	/**
	 * Calculates coordinate of a next letter given a current 
	 * letter and a direction to follow. Properly wraps coordinate 
	 * if wrap flag is set.
	 * 
	 * @param letter Coordinate of current letter being examined
	 * @param relation String that denotes the direction to keep 
	 * 				   looking in the grid
	 * @return 
	 */
	public Coordinate followDirection(Coordinate letter, String relation) {
		int currentRow = letter.getRow();
		int currentCol = letter.getCol();
		Coordinate next = new Coordinate();
		if (!wrap) {
			if (relation.equals("topleft")) { next.setRow(currentRow + DECREMENT); next.setCol(currentCol + DECREMENT); }
			else if (relation.equals("top")) { next.setRow(currentRow + DECREMENT); next.setCol(currentCol); }
			else if (relation.equals("topright")) { next.setRow(currentRow + DECREMENT); next.setCol(currentCol + INCREMENT); }
			else if (relation.equals("left")) { next.setRow(currentRow); next.setCol(currentCol + DECREMENT);  }
			else if (relation.equals("right")) { next.setRow(currentRow); next.setCol(currentCol + INCREMENT);  }
			else if (relation.equals("bottomleft")) { next.setRow(currentRow + INCREMENT); next.setCol(currentCol + DECREMENT);  }
			else if (relation.equals("bottom")) { next.setRow(currentRow + INCREMENT); next.setCol(currentCol);  }
			else if (relation.equals("bottomright")) { next.setRow(currentRow + INCREMENT); next.setCol(currentCol + INCREMENT);  }
			}
		else {
			if (relation.equals("topleft")) { next.setRow(wrapAdd(currentRow, DECREMENT, n)); next.setCol(wrapAdd(currentCol, DECREMENT, m)); }
			else if (relation.equals("top")) { next.setRow(wrapAdd(currentRow, DECREMENT, n)); next.setCol(currentCol); }
			else if (relation.equals("topright")) { next.setRow(wrapAdd(currentRow, DECREMENT, n)); next.setCol(wrapAdd(currentCol, INCREMENT, m)); }
			else if (relation.equals("left")) { next.setRow(currentRow); next.setCol(wrapAdd(currentCol, DECREMENT, m));  }
			else if (relation.equals("right")) { next.setRow(currentRow); next.setCol(wrapAdd(currentCol, INCREMENT, m));  }
			else if (relation.equals("bottomleft")) { next.setRow(wrapAdd(currentRow, INCREMENT, n)); next.setCol(wrapAdd(currentCol, DECREMENT, m));  }
			else if (relation.equals("bottom")) { next.setRow(wrapAdd(currentRow, INCREMENT, n)); next.setCol(currentCol);  }
			else if (relation.equals("bottomright")) { next.setRow(wrapAdd(currentRow, INCREMENT, n)); next.setCol(wrapAdd(currentCol, INCREMENT, m));  }
		}
		if (isValid(next)) {
			return next;
		} else {
			return null;
		}
	}
	
	
	/**
	 * When traversing a word grid that allows wrapping, this 
	 * method performs proper calculations to wrap around when 
	 * reaching an edge.
	 * 
	 * @param coord Either the row or column of the coordinate
	 * 				being traveled from.
	 * @param num Number of steps to take in some direction, with 
	 * 			  negative denoting either top or left, and positive 
	 * 			  denoting either bottom or right.
	 * @param dimension Axis to travel.
	 * @return
	 */
	public int wrapAdd (int coord, int num, int dimension) {
		int val = coord + num;
		if (val == dimension) { // returns to 0 because we're continuing past bottom/right edge
			return 0;
		} else if (val < 0) { // returns to edge because we're continuing past the top/left edge
			return dimension-1;
		} else {
			return val;
		}
	}
	
	
	/**
	 * Given two adjacent Coordinates, this method returns 
	 * a string denoting the second coordinate's relative 
	 * position to the first coordinate.
	 * 
	 * @param start Starting coordinate
	 * @param adjacent Coordinate being traversed toward
	 * @return
	 */
	public String getRelation(Coordinate start, Coordinate adjacent) {
		String relation = "";
		int xDifference = start.getRow() - adjacent.getRow();
		int yDifference = start.getCol() - adjacent.getCol();
		if (xDifference == -1) {
			relation+="bottom";
		} else if (xDifference == 1) { 
			relation+="top";
		} else if (n > 1 && start.getRow() == n-1 && adjacent.getRow()==0) {
			relation+="bottom";
		} else if (n > 1 && start.getRow() == 0 && adjacent.getRow()==n-1) {
			relation+="top";
		}
		if (yDifference == -1) {
			relation+="right";
		} else if (yDifference == 1) {
			relation+="left";
		} else if (m > 1 && start.getCol() == m-1 && adjacent.getCol()==0) {
			relation+="right";
		} else if (m > 1 && start.getCol() == 0 && adjacent.getCol()==m-1) {
			relation+="left";
		}
		return relation;
	}
	
	
	/**
	 * Checks to see if a given coordinate is within
	 * the bounds of the grid.
	 * 
	 * @param c Coordinate being examined
	 * @return
	 */
	public boolean isValid(Coordinate c) {
		return (c.getRow() < n && c.getRow() >= 0 && c.getCol() <m && c.getCol() >=0);
	}
	
	
	/**
	 * Method that, given a coordinate, finds all of its 
	 * adjacent coordinates
	 * 
	 * @param c Coordinate for which adjacent coordinates 
	 * 			are being found
	 * @return
	 */
	public ArrayList<Coordinate> getAdjacents(Coordinate c) {
		ArrayList<Coordinate> adjacents = new ArrayList<Coordinate>();
		adjacents.add(followDirection(c, "topleft"));
		adjacents.add(followDirection(c, "top"));
		adjacents.add(followDirection(c, "topright"));
		adjacents.add(followDirection(c, "left"));
		adjacents.add(followDirection(c, "right"));
		adjacents.add(followDirection(c, "bottomleft"));
		adjacents.add(followDirection(c, "bottom"));
		adjacents.add(followDirection(c, "bottomright"));
		return adjacents;
	}
}
