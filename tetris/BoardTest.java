package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	private Board b3x6,b7x7;
	private Piece square, stick, s1, s2, pyr1, pieceL1, pieceL2;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b3x6 = new Board(3, 6);
		b7x7 = new Board(7, 7);

		b7x7.commit();
		b3x6.commit();

		square = new Piece(Piece.SQUARE_STR);
		stick = new Piece(Piece.STICK_STR);
		s1 = new Piece(Piece.S1_STR);
		s2 = new Piece(Piece.S2_STR);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pieceL1 = new Piece(Piece.L1_STR);
		pieceL2 = new Piece(Piece.L2_STR);
	}
	
//	// Check the basic width/height/max after the one placement
//	@Test
//	public void testSample1() {
//		assertEquals(1, b.getColumnHeight(0));
//		assertEquals(2, b.getColumnHeight(1));
//		assertEquals(2, b.getMaxHeight());
//		assertEquals(3, b.getRowWidth(0));
//		assertEquals(1, b.getRowWidth(1));
//		assertEquals(0, b.getRowWidth(2));
//	}
//
//	// Place sRotated into the board, then check some measures
//	@Test
//	public void testSample2() {
//		b.commit();
//		int result = b.place(sRotated, 1, 1);
//		assertEquals(Board.PLACE_OK, result);
//		assertEquals(1, b.getColumnHeight(0));
//		assertEquals(4, b.getColumnHeight(1));
//		assertEquals(3, b.getColumnHeight(2));
//		assertEquals(4, b.getMaxHeight());
//	}

	@Test
	public void placeTest1() {
		b3x6.commit();
		assertEquals(Board.PLACE_OK,b3x6.place(square,0,0));
		assertEquals(b3x6.getWidth(),3);
		assertEquals(b3x6.getHeight(),6);
		assertEquals(b3x6.getMaxHeight(), 2);
		assertTrue(b3x6.getGrid(1,1) && b3x6.getGrid(0,0)
				&& b3x6.getGrid(0,1) && b3x6.getGrid(1,0));
		b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(stick, 2,0));
		assertEquals(b3x6.getMaxHeight(), 4);
		assertTrue(b3x6.getGrid(2,0) && b3x6.getGrid(2,1)
				&& b3x6.getGrid(2,2) && b3x6.getGrid(2,3));
		b3x6.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, b3x6.place(pyr1,3,3));
	}

	@Test
	public void placeTest2() {
		b3x6.commit();
		assertEquals(Board.PLACE_OK,b3x6.place(stick,0,0));
		assertEquals(b3x6.getMaxHeight(), 4);
		assertTrue(b3x6.getGrid(0,1) && b3x6.getGrid(0,2)
				&& b3x6.getGrid(0,3) && b3x6.getGrid(0,0));
		b3x6.commit();
		assertEquals(Board.PLACE_OK, b3x6.place(square, 0,4));
		assertEquals(b3x6.getMaxHeight(), 6);
		b3x6.commit();
		assertEquals(b3x6.getRowWidth(0),1);
		assertEquals(b3x6.getRowWidth(5),2);
		assertEquals(b3x6.getColumnHeight(0),6);
		b3x6.commit();
		assertEquals(Board.PLACE_BAD,b3x6.place(pyr1,0,0));
		b3x6.undo();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(pieceL1, 1, 0));
		b3x6.commit();
		Piece reverseL1 = pieceL1.computeNextRotation().computeNextRotation();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(reverseL1, 1, 1));
	}

	@Test
    public void clearRowsTest1() {
        b3x6.commit();
        b3x6.place(square, 0,0);
        b3x6.commit();
        b3x6.place(stick, 2, 0);
        b3x6.commit();
        b3x6.clearRows();
        b3x6.commit();
        assertTrue(b3x6.getGrid(2,0) && b3x6.getGrid(2,1)
                && !b3x6.getGrid(0,0));

    }

	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	
}
