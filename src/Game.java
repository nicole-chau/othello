import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {
    
    public void run() {
        final JFrame frame = new JFrame("Othello");
        frame.setLocation(300, 300);
        
        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Loading...");
        status_panel.add(status);
        
        // Game board
        final GameBoard gBoard = new GameBoard(status);
        gBoard.setBackground(new Color(124, 205, 124));
        frame.add(gBoard, BorderLayout.CENTER);
        
        // Control Panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        // Control Panel Buttons
        final JPanel control_buttons = new JPanel();
        control_buttons.setLayout(new GridLayout(2, 4));
        
        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gBoard.instructions();
            }
        });
        control_buttons.add(instructions);
        
        // Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gBoard.reset();
            }
        });
        control_buttons.add(reset);
        
        // Play button
        final JButton play = new JButton("Play");
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gBoard.play();
            }
        });
        control_buttons.add(play);
        
        // Undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gBoard.undo();
            }
        });
        control_buttons.add(undo);
        
        // Skip button
        final JButton skip = new JButton("Skip Turn");
        skip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gBoard.skip();
            }
        });
        control_buttons.add(skip);
        
        
        // Save button
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gBoard.save();
            }
        });
        control_buttons.add(save);
        
        // Load button
        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gBoard.load();
            }
        });
        control_buttons.add(load);
        
        control_panel.add(control_buttons);
        
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        gBoard.reset();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
