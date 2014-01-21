package GUI;

import javax.swing.*;
import java.awt.*;

public class RolitView extends JFrame {
    private ButtonListener buttonListener = new ButtonListener(this);

    private JButton[] buttons = new JButton[64];

    private JMenuItem exitItem;

    public static void main(String[] args) {
        new RolitView().setupFrame();
    }

    public JButton[] getButtons() {
        return buttons;
    }

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
