package client.GUI;

import client.Game;

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
    private Game game;

    public ButtonListener(RolitView rolitView, Game game) {
        this.rolitView = rolitView;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (rolitView.getExitItem().equals(event.getSource())) {
            rolitView.dispatchEvent(new WindowEvent(rolitView, WindowEvent.WINDOW_CLOSING));
        }

        JButton[] buttons = rolitView.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals(event.getSource())) {
                int[] coordinates = game.getBoard().getCoordinates(i);
                game.takeTurn(coordinates[0], coordinates[1]);
            }
        }
    }
}