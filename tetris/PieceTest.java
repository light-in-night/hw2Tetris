package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
@SuppressWarnings("Duplicates")
public class PieceTest {
	private static final String[] CTOR_STRINGS =
			{Piece.L1_STR,Piece.L2_STR,Piece.PYRAMID_STR,Piece.SQUARE_STR,Piece.STICK_STR,Piece.S1_STR,Piece.S2_STR};

	private Piece square, stick, s1, s2, pyr1, pieceL1, pieceL2;

	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		square = new Piece(Piece.SQUARE_STR);
		stick = new Piece(Piece.STICK_STR);
		s1 = new Piece(Piece.S1_STR);
		s2 = new Piece(Piece.S2_STR);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pieceL1 = new Piece(Piece.L1_STR);
		pieceL2 = new Piece(Piece.L2_STR);
	}

	@Test(expected = RuntimeException.class)
	public void ctorTest1() {
		new Piece("0 0 1 1 0 0 1 ");
	}

	@Test(expected = RuntimeException.class)
	public void ctorTest2() {
		new Piece("0 0 1 1 0 0x 01 ");
	}

	@Test
	public void ctorTest3() {
		Piece custom1 = new Piece("0 0 1 1 1 1 2 2");
		Piece custom2 = new Piece("1 1 0 0 2 2 1 1");
		Piece custom3 = new Piece("0 0 1 1 1 1 9 9");
		Piece custom4 = new Piece("1 1 0 0 2 2 5 -1");
	}

	@Test
	public void getHeightTest() {
		assertEquals(2, pyr1.getHeight());

		assertEquals(2, square.getHeight());

		assertEquals(3, pieceL1.getHeight());

		assertEquals(3, pieceL1.getHeight());

		assertEquals(4, stick.getHeight());

		assertEquals(2, s1.getHeight());

		assertEquals(2, s2.getHeight());
	}

	@Test
	public void getWidthTest() {
		assertEquals(3, pyr1.getWidth());

		assertEquals(2, square.getWidth());

		assertEquals(2, pieceL1.getWidth());

		assertEquals(2, pieceL1.getWidth());

		assertEquals(1, stick.getWidth());

		assertEquals(3, s1.getWidth());

		assertEquals(3, s2.getWidth());
	}


	@Test
	public void getSkirtTest() {
		assertArrayEquals(new int[]{0, 0, 0}, pyr1.getSkirt());

		assertArrayEquals(new int[]{0, 0}, square.getSkirt());

		assertArrayEquals(new int[]{0, 0}, pieceL1.getSkirt());

		assertArrayEquals(new int[]{0, 0}, pieceL2.getSkirt());

		assertArrayEquals(new int[]{0, 0, 1}, s1.getSkirt());

		assertArrayEquals(new int[]{1, 0, 0}, s2.getSkirt());

		assertArrayEquals(new int[]{0}, stick.getSkirt());
	}

	@Test
	public void fastRotationTest() {
		assertTrue(s1.fastRotation().fastRotation().equals(s1));
		assertTrue(s2.fastRotation().fastRotation().equals(s2));

		assertEquals(2,s2.fastRotation().getWidth());
		assertEquals(3,s2.fastRotation().getHeight());

		assertEquals(1, stick.fastRotation().getHeight());

		assertTrue(stick.fastRotation().fastRotation().equals(stick));

		Piece p = new Piece(Piece.PYRAMID_STR);
		for(int i = 0; i < 20; i++) {
			p = p.fastRotation();
		}
		assertTrue(p.equals(pyr1));
	}

	@Test
	public void equalsTest() {
		for(String ctorStrA : CTOR_STRINGS) {
			for(String ctorStrB : CTOR_STRINGS) {
				Piece tmpA = new Piece(ctorStrA);
				Piece tmpB = new Piece(ctorStrB);
				if(ctorStrA.equals(ctorStrB))
					assertEquals(tmpA, tmpB);
				else
					assertNotEquals(tmpA, tmpB);
			}
		}
	}

    @Test
	public void getPiecesTest() {
		//Make sure every piece is in the array
		Piece[] pieces = Piece.getPieces();
		for (String ctorStr : CTOR_STRINGS) {
			assertTrue(Arrays.stream(pieces).anyMatch(piece -> piece.equals(new Piece(ctorStr))));
		}

		//Make sure every piece is same after 4 fastRotations
		pieces = Piece.getPieces();
		for (int i = 0; i < pieces.length; i++) {
			for (int rot = 0; rot < 4; rot++) {
				pieces[rot] = pieces[rot].fastRotation();
			}
		}
		Piece[] startingPieces = Piece.getPieces();
		for (int i = 0; i < pieces.length; i++) {
			assertEquals(pieces[i], startingPieces[i]);
		}

		//Make sure that some pieces are same after 2 rotations
		pieces = Piece.getPieces();
		List<Piece> twoCyclicPieces = new ArrayList<>(Arrays.asList(new Piece(Piece.STICK_STR),
				new Piece(Piece.S1_STR),
				new Piece(Piece.S2_STR),
				new Piece(Piece.SQUARE_STR)));
		for (Piece p : pieces) {
			if (twoCyclicPieces.contains(p)) {
				assertEquals(p.fastRotation().fastRotation(), p);
			}
		}
	}

	@Test
	public void getBodyTest() {
		Piece p = new Piece(Piece.SQUARE_STR);
		TPoint[] tps = p.getBody();
		//Expecting to have a square body, create our own
		//and compare
		TPoint[] correctTps = new TPoint[]{new TPoint(0, 0), new TPoint(0, 1),
				new TPoint(1, 0), new TPoint(1, 1)};
		for (TPoint tp : correctTps) {
			assertTrue(Arrays.stream(tps).anyMatch(point -> point.equals(tp)));
		}

		p = new Piece(Piece.S1_STR);
		tps = p.getBody();

		correctTps = new TPoint[]{new TPoint(0, 0),
				new TPoint(1, 0),
				new TPoint(1, 1),
				new TPoint(2, 1)};
		for (TPoint tp : correctTps) {
			assertTrue(Arrays.stream(tps).anyMatch(point -> point.equals(tp)));
		}

		p = new Piece(Piece.PYRAMID_STR);
		tps = p.getBody();
		correctTps = new TPoint[]{new TPoint(0, 0),
				new TPoint(1, 0),
				new TPoint(2, 0),
				new TPoint(1, 1)};
		for (TPoint tp : correctTps) {
			assertTrue(Arrays.stream(tps).anyMatch(point -> point.equals(tp)));
		}

		p = new Piece(Piece.STICK_STR);
		tps = p.getBody();
		correctTps = new TPoint[]{new TPoint(0, 0),
				new TPoint(0, 1),
				new TPoint(0, 2),
				new TPoint(0, 3)};
		for (TPoint tp : correctTps) {
			assertTrue(Arrays.stream(tps).anyMatch(point -> point.equals(tp)));
		}
	}
}
