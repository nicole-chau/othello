import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    
    private Othello o;
    private JLabel status;

    public static final int BOARD_WIDTH = 480;
    public static final int BOARD_HEIGHT = 480;
    
    private static final String NO_MOVES = "No moves to play.";
    private static final String MULTIPLE_DISCS = "Only 1 disc can be played each turn.";
    private static final String INVALID_MOVE = "Invalid move.";
    private static final String GAME_OVER = "Game over; no more moves can be played. "
            + "Click 'restart' to start a new game.";
    private static final String SAVE_SUCCESS = "Game saved successfully!";
    private static final String SAVE_ERROR = "Game could not be saved.";
    private static final String LOAD_ERROR = "Game could not be loaded.";
    
    public GameBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34)));
        
        setFocusable(true);
        
        o = new Othello();
        status = statusInit;

        /*
         * Listens for mouseclicks.  Updates the model, then updates the game board
         * based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                                
                // updates the model given the coordinates of the mouseclick
                o.tryCell(p.y / 60, p.x / 60);
                                
                repaint(); // repaints the game board
            }
        });
    }
    
    final JFrame frame = new JFrame("Error");

    public void instructions() {
        String instr = 
              "Player 1: black discs\n"
              + "Player 2: white discs\n"
              + "Objective of the game: to have as many of the discs on the board\n"
              + "be flipped to your color. A valid move occurs when a player\n"
              + "places their color disc next to an opposite colored disc\n"
              + "such a row/column/diagonal of the opponent's colored discs\n"
              + "are immediately between 2 discs of the player's disc color. \nWhen "
              + "this occurs, all of the opponent's discs in between will be\n"
              + "flipped to match the player's disc color.\n"
              + "If there are no more possible moves for either player the game ends.\n\n"
              + "You must click 'Play' to actually play your move.";
        JOptionPane.showMessageDialog(frame, instr, "Instructions", 
                JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Resets the game to its initial state.
     */
    public void reset() {
        o.reset();
        status.setText("Player 1's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    
    /**
     * plays the turn
     */
    
    public void play() {
        int success = o.playTurn(o.getMoves());

        if (success == -1) {
            JOptionPane.showMessageDialog(frame, NO_MOVES, "Error", 
                    JOptionPane.PLAIN_MESSAGE);
        } else if (success == 1) {
            JOptionPane.showMessageDialog(frame, MULTIPLE_DISCS, "Error", 
                    JOptionPane.PLAIN_MESSAGE);
        } else if (success == 2) {
            JOptionPane.showMessageDialog(frame, INVALID_MOVE, "Error", 
                    JOptionPane.PLAIN_MESSAGE);
        } else if (success == 3) {
            JOptionPane.showMessageDialog(frame, GAME_OVER, "Error", 
                    JOptionPane.PLAIN_MESSAGE);
        }
        
        repaint();
               
        updateStatus(); // updates the status JLabel
    }
    

    public void undo() {
        o.undo();
        repaint();
    }
    
    public void skip() {
        o.skip();
        repaint();
        updateStatus();
    }
    
    public void save() {
        boolean success = o.save();
        if (success) {
            JOptionPane.showMessageDialog(frame, SAVE_SUCCESS, "Success", 
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, SAVE_ERROR, "Error", 
                    JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    public void load() {
        boolean success = o.load();
        if (!success) {
            JOptionPane.showMessageDialog(frame, LOAD_ERROR, "Error", 
                    JOptionPane.PLAIN_MESSAGE);
        }
        repaint();
        updateStatus();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (o.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
        } else {
            status.setText("Player 2's Turn");
        }
        
        int winner = o.endGame();
        if (winner == 1) {
            status.setText("Player 1 wins! | Player 1 Score: " 
                    + o.getP1Score() + " | Player 2 Score: " + o.getP2Score());
        } else if (winner == 2) {
            status.setText("Player 2 wins! | Player 1 Score: " 
                    + o.getP1Score() + " | Player 2 Score: " + o.getP2Score());
        } else if (winner == 3) {
            status.setText("It's a tie! | Player 1 Score: " 
                    + o.getP1Score() + " | Player 2 Score: " + o.getP2Score());
        }
    }
    
    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
                
        drawGrid(g);
        drawDiscs(g);       
    }
    
    private void drawGrid(Graphics g) {
        g.setColor(new Color(34, 139, 34));
        // vertical
        g.drawLine(60, 0, 60, 480);
        g.drawLine(120, 0, 120, 480);
        g.drawLine(180, 0, 180, 480);
        g.drawLine(240, 0, 240, 480);
        g.drawLine(300, 0, 300, 480);
        g.drawLine(360, 0, 360, 480);
        g.drawLine(420, 0, 420, 480);
        //horizontal        
        g.drawLine(0, 60, 480, 60);
        g.drawLine(0, 120, 480, 120);
        g.drawLine(0, 180, 480, 180);
        g.drawLine(0, 240, 480, 240);
        g.drawLine(0, 300, 480, 300);
        g.drawLine(0, 360, 480, 360);
        g.drawLine(0, 420, 480, 420);  
    }
    
    private void drawDiscs(Graphics g) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int disc = o.getCell(row, col);
                if (disc == 1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(col * 60 + 10, row * 60 + 10, 40, 40);
                } else if (disc == 2) {
                    g.setColor(Color.WHITE);   
                    g.fillOval(col * 60 + 10, row * 60 + 10, 40, 40);
                }
            }
        }
    }
    
    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
    
}
