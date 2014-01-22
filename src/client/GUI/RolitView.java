package client.GUI;

import client.Game;
import client.Mark;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class RolitView extends JFrame implements Observer {
    private ButtonListener buttonListener;

    private JButton[] buttons = new JButton[64];

    private JMenuItem exitItem;

    /**
     * Constructor.
     * Sets up the main view with the game board.
     */
    public RolitView(Game game) {
        buttonListener = new ButtonListener(this, game);
        setupFrame();
    }

    /**
     * Returns an array of <code>JButton</code> buttons for the 8x8 game board.
     *
     * @return buttons Array of buttons
     */
    public JButton[] getButtons() {
        return buttons;
    }

    /**
     * Returns the <code>JMenuItem</code> of the exit button.
     *
     * @return exitItem menuItem button
     */
    public JMenuItem getExitItem() {
        return exitItem;
    }

    private void setupFrame() {
        //Add buttons
        JPanel buttonPanel = new JPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        add(buttonPanel);

        buttonPanel.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 64; i++) {
            JButton rolitButton = new JButton();
            buttonPanel.add(rolitButton);
            buttons[i] = rolitButton;
            rolitButton.addActionListener(buttonListener);
        }

        //Add menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(buttonListener);

        file.add(exitItem);
        menuBar.add(file);

        setVisible(true);
    }

    /**
     * Update the GUI buttons with the current state of the fields.
     * This method is called via the <code>notifyObservers()</code> method in game.
     *
     * @param observable the game controller
     * @param arg
     */
    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Game) {
            Game game = (Game) observable;
            for (int i = 0; i < buttons.length; i++) {
                Mark mark = game.getBoard().getField(i);
                if (mark != Mark.EMPTY) {
                    buttons[i].setBackground(mark.getColor());
                    buttons[i].setEnabled(false);
                }
            }
        }
    }
}
