package tetris;

import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris {
    JCheckBox brainMode;
    JCheckBox animateMode;
    JLabel brainModeLabel;
    Brain.Move bestMove;

    JLabel adversaryLabel;
    JSlider adversarySlider;
    JLabel adversaryStatus;

    DefaultBrain defBrain;
    
    /**
     * Adds a new piece AND
     * calculates the best place for that piece to go to.
     * This calculation takes place only once per piece.
     */
    @Override
    public void addNewPiece() {
  		super.addNewPiece();
  		if(brainMode.isSelected()) {
			super.board.undo();
			defBrain.bestMove(super.board, super.currentPiece, super.getHeight()-TOP_SPACE, bestMove);
	  		super.board.undo();
  		}
    }
    
    /**
     * Calls super.ctor with int pixels.
     * @param pixels width of a single Tetris block in
     * pixels.
     */
    public JBrainTetris(int pixels) {
        super(pixels);
        defBrain = new DefaultBrain();

        brainMode = new JCheckBox("Brain active");
        animateMode = new JCheckBox("Animate fall");
        brainModeLabel = new JLabel("Brain: ");

        bestMove = new Brain.Move();

        adversaryLabel = new JLabel("Adversary");
        adversarySlider = new JSlider(0, 100, 0);
        adversarySlider.setPreferredSize(new Dimension(100,15));
        adversaryStatus = new JLabel("Ok");
    }
    
    /**
     * Adds JBrainTetris functionality on GUI
     */
    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();
        panel.add(brainModeLabel);
        panel.add(brainMode);
        panel.add(animateMode);
        panel.add(adversaryLabel);
        panel.add(adversarySlider);
        panel.add(adversaryStatus);
        return panel;
    }
    
    /**
     * When adversary is enabled, a random integer in
     * [0, 100] range is compared to the value of slider.
     * if rand value is more, piece is random.
     * else piece is a real PIECE OF SHEEP.
     */
    @Override
    public Piece pickNextPiece() {
        if(random.nextInt(101) >= adversarySlider.getValue()) {
            adversaryLabel.setText("ok");
            return super.pickNextPiece();        
        } else {
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
        }
    }
    
    /**
     * Adds Brain functionality.
     * Rotates and/or moves a piece by one step, whenever
     * brain is enabled from GUI AND the verb is JTetris.DOWN.
     */
    @Override
    public void tick(int verb) {
        if(verb == JTetris.DOWN && brainMode.isSelected()) {
        	if(!currentPiece.equals(bestMove.piece)) {
                super.tick(ROTATE);
            }
        	//TODO: Rotating problem, pieces aren't the same AT ALL!
            if(currentX != bestMove.x) {
                super.tick(bestMove.x > super.currentX ? RIGHT : LEFT);
            } else {
            	if(currentPiece.equals(bestMove.piece)
            			&& !animateMode.isSelected()) {
            		super.tick(DROP);
            		super.tick(DOWN);
            	}
            }          
        }
        super.tick(verb);
    }
    
    /**
     * Program entry point.
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JTetris tetrisWithBrain = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetrisWithBrain);
        frame.setVisible(true);
    }
}
