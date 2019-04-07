// Board.java
package tetris;

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	private boolean committed;

    private int[] widths;
	private int[] heights;
    private int maxHeight;

	private boolean[][] backupGrid;
	private int[]  backupWidths;
    private int[]  backupHeights;
    private int  backupMaxHeight;
    
    private boolean[][] crGrid;
	private int[]  crWidths;
    private int[]  crHeights;
    
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;

        widths = new int[height];
        heights = new int[width];

        backupGrid = new boolean[width][height];
        backupWidths = new int[height];
        backupHeights = new int[width];
        
        crGrid = new boolean[width][height];
        crWidths = new int[height];
        crHeights = new int[width];
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	 * 
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int calculatedMaxHeight = 0;
			int[] calculatedWidths = new int[grid[0].length];
			int[] calculatedHeights = new int[grid.length];
			for(int x = 0; x < grid.length; x++) {
				for(int y = 0; y < grid[0].length; y++) {
					if(grid[x][y]) {
						calculatedHeights[x] = Math.max(calculatedHeights[x], y+1);
						calculatedWidths[y]++;
						calculatedMaxHeight = Math.max(calculatedMaxHeight, y+1);
					}
				}
			}
			
//			assert Arrays.equals(calculatedHeights, heights)
//					&& Arrays.equals(calculatedWidths, widths);
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int result = 0;
		int[] skirt = piece.getSkirt();
		for(int i = 0; i < skirt.length; i++) {
			result = Math.max(heights[x + i]-skirt[i],result);
		}
		return result;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x < 0 || x >= getWidth() 
				|| y < 0 
				|| y >= getHeight())
			return true;
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

        TPoint[] body = piece.getBody();

        for(TPoint tp : body) {
        	if(x + tp.x < 0 || x + tp.x >= width
				|| y + tp.y < 0 || y + tp.y >= height)
        		return PLACE_OUT_BOUNDS;
            if(grid[x + tp.x][y + tp.y])
                return PLACE_BAD;
        }

        backUp();
		committed = false;
		return updateState(body,x,y);
	}

    /**
     * Does all the hard work of redrawing the piece
     * into the inner grid structure. also updates
     * heights and widths arrays and maxHeight.
     * @param body points representing a piece to be added
     * @param x where to place the piece x coordinate
     * @param y where to place the piece, y coordinate
     * @return a state variable may be PLACE_OK or PLACE_ROW_FILLED
     */
    private int updateState(TPoint[] body, int x, int y) {
        int result = PLACE_OK;
        for(TPoint tp : body) {
            grid[x + tp.x][y + tp.y] = true;
            heights[x + tp.x] = Math.max(heights[x + tp.x], y + tp.y + 1);
            maxHeight = Math.max(maxHeight, y + tp.y + 1);
            widths[y + tp.y]++;
            if(widths[y + tp.y] == getWidth())
                result = PLACE_ROW_FILLED;
        }
        return result;
    }

    /**
     * Saves grid[][], widths[], heights[] into
     * backupGrid[][], backupWidths[], backupHeights[].
     * Does not allocate new memory.
     */
    private void backUp() {
	    System.arraycopy(heights,0,backupHeights,0, backupHeights.length);
        System.arraycopy(widths,0,backupWidths,0, backupWidths.length);
        backupMaxHeight = maxHeight;
	    for(int x = 0; x < width; x++)
	        System.arraycopy(grid[x],0, backupGrid[x],0, height);
    }

    /**
	 * Deletes rows that are filled all the way across, moving
     * things above down. Returns the number of rows cleared.
	 */
	public int clearRows() {
        if(committed)
        	backUp();
        committed = false;
        int rowsCleared = 0;

        backupToCRArrays();
        
        for(int x = 0; x < width; x++) {
			heights[x] = clearShiftDownGrid(x);
        }

		shiftDownWidths();

		for(int y = 0; y < maxHeight; y++) {
            if(crWidths[y] == width)
                rowsCleared++;
        }

        maxHeight -= rowsCleared;
		sanityCheck();
		return rowsCleared;
	}
	
	/**
	 * Copies data from
	 * the grid[][], widths[] and heights[]
	 * to crGrid[][], crWidths[], crHeights[].
	 * Does not allocate new memory.
	 * Used for fast computing clearRows
	 */
	private void backupToCRArrays() {
	    System.arraycopy(heights,0,crHeights,0, crHeights.length);
        System.arraycopy(widths,0,crWidths,0, crWidths.length);
	    for(int x = 0; x < width; x++)
	        System.arraycopy(grid[x],0, crGrid[x],0, height);
	}

	/**
	 * Clears and shifts down this.widths
	 * according to the state present in this.backupWidths.
	 */
	private void shiftDownWidths() {
		int from = 0;
		for(int to = 0; to < height; to++) {
			while(from < height
					&& crWidths[from] == width) {
				from++;
			}
			if(from < height) {
				widths[from] = 0;
				widths[to] = crWidths[from];
			} else {
				widths[to] = 0;
			}
			from++;
		}
	}


	/**
	 * @param x the x coordinate of the
	 *          column to clear in this.grid
	 * @return number of rows removed by the clearing process.
	 */
	private int clearShiftDownGrid(int x) {
		int from = 0;
		int newHeight = 0;
		for(int to = 0; to < crHeights[x]; to++) {
			while(from < crHeights[x]
					&& crWidths[from] == width) {
				from++;
			}
			if(from < crHeights[x]) {
				grid[x][from] = false;
				grid[x][to] = crGrid[x][from];
				if(crGrid[x][from]) {
					newHeight = to + 1;
				}
			} else {
				grid[x][to] = false;
			}
			from++;
		}
		return newHeight;
	}


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed) return;
        maxHeight = backupMaxHeight;

        boolean[][] tmp = grid;
        grid = backupGrid;
        backupGrid = tmp;

        int[] tmpWidths = widths;
        widths = backupWidths;
        backupWidths = tmpWidths;

        int[] tmpHeights = heights;
        heights = backupHeights;
        backupHeights = tmpHeights;

		committed = true;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
        if(committed) return;
        backUp();
        committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


