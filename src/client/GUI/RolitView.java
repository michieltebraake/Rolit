package client.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class RolitView extends JFrame {
    private ButtonListener buttonListener = new ButtonListener(this);

    private JButton[] buttons = new JButton[64];

    private JMenuItem exitItem;

    public static void main(String[] args) {
        new RolitView().setupFrame();
    }

    /**
     * Returns an array of <code>JButton</code> buttons for the 8x8 game board.
     * @return buttons Array of buttons
     */
    public JButton[] getButtons() {
        return buttons;
    }

    /**
     * Returns the <code>JMenuItem</code> of the exit button.
     * @return exitItem menuItem button
     */
    public JMenuItem getExitItem(){
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
}
