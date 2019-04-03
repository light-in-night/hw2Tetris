package tetris;

import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris {


    JCheckBox brainMode;
    JLabel brainModeLabel;
    Brain.Move bestMove;

    JLabel adversaryLabel;
    JSlider adversarySlider;
    JLabel adversaryStatus;

    DefaultBrain defBrain;
    public JBrainTetris(int pixels) {
        super(16);
        defBrain = new DefaultBrain();

        brainMode = new JCheckBox("Brain active");
        brainModeLabel = new JLabel("Brain: ");

        bestMove = new Brain.Move();

        adversaryLabel = new JLabel("Adversary");
        adversarySlider = new JSlider(0, 100, 0);
        adversarySlider.setPreferredSize(new Dimension(100,15));
        adversaryStatus = new JLabel("Ok");
    }

    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();
        panel.add(brainModeLabel);
        panel.add(brainMode);
        panel.add(adversaryLabel);
        panel.add(adversarySlider);
        panel.add(adversaryStatus);
        return panel;
    }

    @Override
    public Piece pickNextPiece() {
        if(random.nextInt(101) < adversarySlider.getValue()) {
            adversaryLabel.setText("*ok*");
            Piece worstPiece = pieces[0];
            double worstScore = Double.MIN_VALUE;
            for(Piece p : pieces) {
                board.undo();
                defBrain.bestMove(super.board, p, super.getHeight(), bestMove);
                if(bestMove.score > worstScore) {
                    worstScore = bestMove.score;
                    worstPiece = p;
                }
            }
            board.undo();
            return worstPiece;
        } else {
            adversaryLabel.setText("ok");
            return super.pickNextPiece();
        }
    }

    @Override
    public void tick(int verb) {
        if(verb == JTetris.DOWN
                && brainMode.isSelected()) {

            super.board.undo();
            defBrain.bestMove(super.board, super.currentPiece, super.getHeight(), bestMove);
            super.board.undo();
            if(currentX != bestMove.x) {
                super.tick(bestMove.x > super.currentX ? RIGHT : LEFT);
            }
            if(!currentPiece.equals(bestMove.piece)) {
                super.tick(ROTATE);
            }
        }
        super.tick(verb);
    }

    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JTetris tetrisWithBrain = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetrisWithBrain);
        frame.setVisible(true);
    }
}
