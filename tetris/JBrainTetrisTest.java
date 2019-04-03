package tetris;

import static org.junit.Assert.*;

import org.junit.*;
public class JBrainTetrisTest {
    JTetris tetris;
    JBrainTetris autoTetris;
    DefaultBrain brain;

    @Before
    public void setUp() {
        tetris = new JTetris(16);
        autoTetris = new JBrainTetris(16);
        brain = new DefaultBrain();
    }

    @Test
    public void bestMoveTest1() {
        autoTetris.tick(JTetris.DOWN);
    }
}
