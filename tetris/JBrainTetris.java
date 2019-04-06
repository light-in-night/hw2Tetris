package tetris;

public class JBrainTetris extends JTetris implements Brain {
    public JBrainTetris(int pixels) {
        super(pixels);
    }

    @Override
    public Move bestMove(Board board, Piece piece, int limitHeight, Move move) {
        return null;
    }
}
