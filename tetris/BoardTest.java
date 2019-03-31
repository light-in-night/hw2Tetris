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
	
	@Test
	public void getGridTest() {
		b3x6.commit();
		b3x6.place(square,0,0);
		assertTrue(b3x6.getGrid(-1,b3x6.getHeight()));
		assertTrue(b3x6.getGrid(0,0) && b3x6.getGrid(0,1)
				&& b3x6.getGrid(1,0) && b3x6.getGrid(1,1)
		&& !b3x6.getGrid(2,1) && !b3x6.getGrid(2,5));
	}

	@Test
	public void dropHeightTest1() {
		b3x6.commit();
		assertEquals(0,b3x6.dropHeight(square, 0));
		assertEquals(0,b3x6.dropHeight(s1, 0));
		b3x6.commit();
		b3x6.place(pyr1,0,0);
		assertEquals(1, b3x6.dropHeight(stick, 2));
		assertEquals(1, b3x6.dropHeight(pyr1.computeNextRotation(),1));
		assertEquals(2, b3x6.dropHeight(pyr1.computeNextRotation(),0));
	}

	@Test
	public void dropHeightTest2() {
		b3x6.commit();
		assertEquals(0, b3x6.dropHeight(pyr1, 0));
		assertEquals(Board.PLACE_ROW_FILLED,b3x6.place(pyr1,0,0));
		b3x6.commit();
		assertEquals(Board.PLACE_OK,b3x6.place(pyr1.computeNextRotation(), 1, 1));
		b3x6.commit();
		assertEquals(1, b3x6.dropHeight(pieceL2.computeNextRotation().computeNextRotation(), 0));
		assertEquals(Board.PLACE_ROW_FILLED,b3x6.place(pieceL2.computeNextRotation().computeNextRotation(), 0, 1));
		b3x6.commit();
	}

	@Test
	public void placeTest1() {
		b3x6.commit();
		assertEquals(Board.PLACE_OK,b3x6.place(square,0,0));
		assertEquals(3, b3x6.getWidth());
		assertEquals(6, b3x6.getHeight());
		assertEquals(2,b3x6.getMaxHeight());
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
		assertEquals(4,b3x6.getMaxHeight());
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
	public  void placeTest3() {
		b3x6.commit();
		for(int i = 4; i >= 0; i--) {
			assertEquals(Board.PLACE_OK, b3x6.place(square, 0,i));
			b3x6.undo();
		}
		assertEquals(Board.PLACE_OUT_BOUNDS, b3x6.place(square, -1, 0));
		b3x6.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b3x6.place(square, -1, -1));
		b3x6.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b3x6.place(square, -1, 1));
		b3x6.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b3x6.place(stick.computeNextRotation(), 0, 0));
		b3x6.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b3x6.place(stick.computeNextRotation(), 5, 4));
		b3x6.undo();
	}

	@Test
    public void clearRowsTest1() {
        b3x6.commit();
        b3x6.place(square, 0,0);
        b3x6.commit();
        b3x6.place(stick, 2, 0);
        b3x6.commit();
        assertEquals(2,b3x6.clearRows());
        assertTrue(b3x6.getGrid(2,0) && b3x6.getGrid(2,1)
                && !b3x6.getGrid(0,0) && !b3x6.getGrid(2,2)
                && !b3x6.getGrid(2,3));
        b3x6.commit();
        assertTrue(b3x6.getGrid(2,0) && b3x6.getGrid(2,1)
                && !b3x6.getGrid(0,0));

        //fill up entire grid
        assertEquals(Board.PLACE_ROW_FILLED,b3x6.place(square,0,0));
        b3x6.commit();
        assertEquals(Board.PLACE_OK,b3x6.place(stick, 0,2));
        b3x6.commit();
        assertEquals(Board.PLACE_OK,b3x6.place(stick, 1,2));
        b3x6.commit();
        assertEquals(Board.PLACE_ROW_FILLED,b3x6.place(stick, 2,2));
        b3x6.commit();
        assertEquals(6, b3x6.clearRows());
        assertEquals(0,b3x6.getMaxHeight());
        assertTrue(!b3x6.getGrid(0,0) && !b3x6.getGrid(1,1) &&
				!b3x6.getGrid(0,1) && !b3x6.getGrid(1,0));
		assertEquals(0,b3x6.getMaxHeight());


	}

    @Test
    public void clearRowsTest2() {
        b3x6.commit();
        b3x6.place(square, 1,2);
        b3x6.commit();
        assertEquals(Board.PLACE_ROW_FILLED,b3x6.place(stick, 0, 0));
		assertEquals(2,b3x6.clearRows());
        assertTrue(b3x6.getGrid(0,1) && b3x6.getGrid(0,0)
            && !b3x6.getGrid(1,0) && !b3x6.getGrid(1,1));

        b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED,b3x6.place(square,1,0));
		assertEquals(2,b3x6.clearRows());

		//filling half
		b3x6.commit();
		assertEquals(Board.PLACE_OK, b3x6.place(stick,0,0));
		b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(pieceL1,1,0));
		b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(s2,0,3));
		b3x6.commit();
		assertEquals(2, b3x6.clearRows());
		b3x6.commit();
		assertTrue(!b3x6.getGrid(2, 0) && b3x6.getGrid(0,2)
				&& !b3x6.getGrid(0,3));
    }

	@Test
	public void clearRowsTest3() {
		b3x6.commit();
		assertEquals(Board.PLACE_OK,b3x6.place(pyr1.computeNextRotation(), 1,0));
		b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(square, 0, 2));
		assertEquals(1,b3x6.clearRows());
		assertTrue(!b3x6.getGrid(0, 0) && !b3x6.getGrid(0,1) && !b3x6.getGrid(1, 0)
				&& b3x6.getGrid(0,2)&& b3x6.getGrid(1,2)&& !b3x6.getGrid(2,2));
		b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(pyr1.computeNextRotation(),1,2));
		assertEquals(1,b3x6.clearRows());
		assertTrue(!b3x6.getGrid(0, 2) && b3x6.getGrid(1,2) && b3x6.getGrid(2, 2));
	}


	@Test
	public void clearRowsTest4() {
		b3x6.commit();
		assertEquals(Board.PLACE_OK,b3x6.place(pieceL1, 0,0));
		b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED,
				b3x6.place(pieceL1.computeNextRotation().computeNextRotation(), 1, 0));
		assertEquals(2,b3x6.clearRows());
		assertTrue(b3x6.getGrid(0,0) && !b3x6.getGrid(1,0) && b3x6.getGrid(2,0)
			&& !b3x6.getGrid(1,1) && !b3x6.getGrid(2,2));
	}

	@Test
    public void undoTest1() {
	    b3x6.commit();
	    assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(pyr1,0,0));
	    b3x6.undo();
        assertTrue(!b3x6.getGrid(0, 0) && !b3x6.getGrid(1, 0) && !b3x6.getGrid(0, 1));
        b3x6.undo();
        b3x6.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, b3x6.place(square,-1,0));
		b3x6.undo();
		assertTrue(!b3x6.getGrid(0, 0) && !b3x6.getGrid(1, 0) && !b3x6.getGrid(1, 1));
		b3x6.commit();
	}

    @Test
	public void getMaxHeightTest() {
		b3x6.commit();
		assertEquals(Board.PLACE_OK,b3x6.place(pieceL1, 0,0));
		assertEquals(3,b3x6.getMaxHeight());
		b3x6.commit();
		assertEquals(3,b3x6.getMaxHeight());
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(square, 1,1));
		assertEquals(3,b3x6.getMaxHeight());
		b3x6.commit();
		assertEquals(2,b3x6.clearRows());
		b3x6.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b3x6.place(stick, 2,0));
		assertEquals(4, b3x6.getMaxHeight());
		b3x6.commit();
	}

	@Test
	public void generalTest1() {
		b3x6.commit();
		for(int y = 3; y >= 0; y--) {
			assertEquals(Board.PLACE_OK, b3x6.place(pieceL1, 0, y));
			b3x6.undo();
		}
	}
	//TODO: testing the ToString method.
	//TODO: ADD MORE GENERAL TESTS
}
