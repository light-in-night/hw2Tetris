package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
@SuppressWarnings("Duplicates")
public class PieceTest {
	private static final int TETRIS_PIECE_SIZE = 4;
	private static final String CTOR_DELIMITERS = "\\s";
	private static final String[] CTOR_STRINGS =
			{Piece.L1_STR,Piece.L2_STR,Piece.PYRAMID_STR,Piece.SQUARE_STR,Piece.STICK_STR,Piece.S1_STR,Piece.S2_STR};


	/**
	 * Constructs a piece from a ctor string as a 2d array
	 * of booleans
	 * @param ctorStringA standard representation of the piece
	 * @return 2d array of booleans, representing a Tetris piece
	 */
	private boolean[][] getExpectedObject(String ctorStringA) {
		boolean[][] result = new boolean[TETRIS_PIECE_SIZE][TETRIS_PIECE_SIZE];

		List<Integer> list = getParsedPoints(ctorStringA);
		for(int i = 0; i < list.size(); i+=2) {
			result[list.get(i)]
					[list.get(i+1)] = true;
		}
		return result;
	}

	private List<Integer> getParsedPoints(String ctorString) {
		List<String> list = new ArrayList<>(Arrays.asList(ctorString.split(CTOR_DELIMITERS)));
		list.removeAll(Arrays.asList(""));
		List<Integer> result = new ArrayList<>();
		for(String s : list) {
			result.add(Integer.parseInt(s));
		}
		return result;
	}

	/**
	 * TODO: TESTING THIS!
	 * Calculates a new a piece that is counterclockwise 90 degrees to
	 * the first one.
	 * @param input a 2d array of booleans, representing a piece, x coordinate is
	 *              rows, y is columns.
	 * @return a rotated version the input
	 */
	private boolean[][] getNextRotation(boolean[][] input) {
		boolean[][] result = new boolean[TETRIS_PIECE_SIZE][TETRIS_PIECE_SIZE];
		for(int i = 0; i < input.length; i++) {
			for(int j = 0; j < input[0].length; j++) {
				result[j][input.length - 1 - i] = input[i][j];
			}
		}
		return  result;
	}


	private int getBodyHeight(boolean[][] pieceGrid) {
		int startAt = Integer.MAX_VALUE;
		int endAt = Integer.MIN_VALUE;
		for(int i = 0; i < pieceGrid.length; i++) {
			for(int j = 0; j < pieceGrid[i].length; j++) {
				if(pieceGrid[i][j]) {
					startAt = Math.min(j,startAt);
					endAt = Math.max(j,endAt);
				}
			}
		}
		return endAt - startAt + 1;
	}

	/**
	 * Returns a width of a piece as a 2d grid.
	 * calculated as a maximum length between two true points on
	 * 2d boolean grid.
	 * @param pieceGrid
	 * @return
	 */
	private int getBodyWidth(boolean[][] pieceGrid) {
		return getBodyHeight(getNextRotation(pieceGrid));
	}

	private int[] getBodySkirt(boolean[][] pieceGrid) {
		int[] skirt = new int[getBodyWidth(pieceGrid)];
		for(int i = 0; i < pieceGrid.length; i++) {
			for (int j = 0; j < pieceGrid[i].length; j++) {
				if(pieceGrid[i][j]) {
					skirt[i] = j;
					break;
				}
			}
		}
		return skirt;
	}

	@Test
	public void getHeightTest() {
		Piece p = new Piece(Piece.PYRAMID_STR);
		assertEquals(3, p.getWidth());

		p = new Piece(Piece.SQUARE_STR);
		assertEquals(2, p.getWidth());

		p = new Piece(Piece.L1_STR);
		assertEquals(2, p.getWidth());

		p = new Piece(Piece.L2_STR);
		assertEquals(2, p.getWidth());

		p = new Piece(Piece.S1_STR);
		assertEquals(3, p.getWidth());

		p = new Piece(Piece.S2_STR);
		assertEquals(3, p.getWidth());

		p = new Piece(Piece.STICK_STR);
		assertEquals(1, p.getWidth());

		p = new Piece(Piece.PYRAMID_STR);
		assertEquals(3, p.getWidth());
	}

	@Test
	public void getWidthTest() {
		Piece p = new Piece(Piece.PYRAMID_STR);
		assertEquals(2, p.getHeight());

		p = new Piece(Piece.SQUARE_STR);
		assertEquals(2, p.getHeight());

		p = new Piece(Piece.L1_STR);
		assertEquals(3, p.getHeight());

		p = new Piece(Piece.L2_STR);
		assertEquals(3, p.getHeight());

		p = new Piece(Piece.S1_STR);
		assertEquals(2, p.getHeight());

		p = new Piece(Piece.S2_STR);
		assertEquals(2, p.getHeight());

		p = new Piece(Piece.STICK_STR);
		assertEquals(4, p.getHeight());
	}

	@Test
	public void getSkirtTest() {
		Piece p = new Piece(Piece.PYRAMID_STR);
		assertTrue(Arrays.equals(new int[]{0,0,0},p.getSkirt()));

		p = new Piece(Piece.SQUARE_STR);
		assertTrue(Arrays.equals(new int[]{0,0},p.getSkirt()));

		p = new Piece(Piece.L1_STR);
		assertTrue(Arrays.equals(new int[]{0,0},p.getSkirt()));

		p = new Piece(Piece.L2_STR);
		assertTrue(Arrays.equals(new int[]{0,0},p.getSkirt()));

		p = new Piece(Piece.S1_STR);
		assertTrue(Arrays.equals(new int[]{0,0,1},p.getSkirt()));

		p = new Piece(Piece.S2_STR);
		assertTrue(Arrays.equals(new int[]{1,0,0},p.getSkirt()));

		p = new Piece(Piece.STICK_STR);
		assertTrue(Arrays.equals(new int[]{0},p.getSkirt()));
	}

	//TODO: IMPLEMENT THIS
	@Test
	public void fastRotationTest() {
//	    Piece p = new Piece(Piece.STICK_STR);
//	    Piece rotP = Piece.
	}

	@Test
	public void equalsTest() {
		for(String ctorStrA : CTOR_STRINGS) {
			for(String ctorStrB : CTOR_STRINGS) {
				Piece tmpA = new Piece(ctorStrA);
				Piece tmpB = new Piece(ctorStrB);
				if(ctorStrA.equals(ctorStrB))
					assertTrue(tmpA.equals(tmpB));
				else
					assertFalse(tmpA.equals(tmpB));
			}
		}
	}

    @Test
    public void getPiecesTest() {
	    Piece[] pieces = Piece.getPieces();

    }
}
