import java.io.File;
import java.util.Scanner;


/**
 * 
 * A word search solver that takes as its argument the 
 * name of a file whose format is as follows:
 * 
 * N M
 * N rows of M letters
 * "WRAP" or "NO_WRAP"
 * P
 * P words with 1 word per line
 * 
 * Uses both a two dimensional array and a multi value 
 * hash map to try to maximize time efficiency. The idea is to 
 * solve the word search by iterating through the list of 
 * words, and for each word, look at the first character. 
 * Lookup that character in the hashmap to find all of its 
 * locations in the grid. From there, examine adjacent 
 * letters by looking them up in the 2D array and see if 
 * any correspond to the following letter in the word. If 
 * so, pursue that path to see if the word is found. 
 * 
 * @author Timothy Ho
 * @version $Date: 2011/9/27 2:49:25 $
 *
 */
public class SuperWordSearch {
	
	public static void main(String args[]) {
			try{
				Scanner scanner = new Scanner(new File(args[0]));
				int rows = scanner.nextInt(); // get first line (grid dimensions)
				int columns = scanner.nextInt();
				if (rows < 1 || columns < 1) { throw new Exception("Invalid grid size!"); }
				WordGrid wordgrid = new WordGrid(rows,columns);
				scanner.nextLine();
				for (int i = 0; i < rows; i++) { // get grid lines
					wordgrid.addLine(scanner.nextLine());
				}
				String wrap = scanner.nextLine();
				wordgrid.setWrap(wrap);
				int numWords = scanner.nextInt();
				scanner.nextLine();
				for (int i = 0; i < numWords; i++) { // find words
					String toFind = scanner.nextLine();
					if (toFind.matches("^[a-zA-Z]+$")) {
						wordgrid.findWord(toFind);
					} else {
						throw new Exception("Word " + toFind + " contains nonletter!");
					}
				}
				if (scanner.hasNextLine()) {
					throw new Exception("Found extra content after last word in word list!");
				}
			}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}	
}
