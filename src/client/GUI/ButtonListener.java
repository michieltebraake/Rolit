package client.GUI;

import client.Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class ButtonListener implements ActionListener {
    private RolitView rolitView;
    private Board board;

    public ButtonListener(RolitView rolitView, Board board) {
        this.rolitView = rolitView;
        this.board = board;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (rolitView.getExitItem().equals(event.getSource())) {
            rolitView.dispatchEvent(new WindowEvent(rolitView, WindowEvent.WINDOW_CLOSING));
        } else if (rolitView.getRestartItem().equals(event.getSource())) {
            //TODO Send reset request via protocol
        }

        JButton[] buttons = rolitView.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals(event.getSource())) {
                int[] coordinates = board.getCoordinates(i);
                //game.takeTurn(coordinates[0], coordinates[1]); //TODO Send move via protocol
            }
        }
    }
}
