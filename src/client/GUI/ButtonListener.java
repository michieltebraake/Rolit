package client.GUI;

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

    public ButtonListener(RolitView rolitView) {
        this.rolitView = rolitView;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (rolitView.getExitItem().equals(event.getSource())) {
            rolitView.dispatchEvent(new WindowEvent(rolitView, WindowEvent.WINDOW_CLOSING));
        } else if (rolitView.getRestartItem().equals(event.getSource())) {
            //Protocol does not support restart requests.
        } else if (rolitView.getConnectItem().equals(event.getSource())) {
            new ConnectView(rolitView);
        } else if (rolitView.getShowPossibleMoves().equals(event.getSource())) {
            rolitView.toggleShowMoves();
            rolitView.update(rolitView.getClientConnection(), rolitView.getCanClickButtons());
        }

        JButton[] buttons = rolitView.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals(event.getSource())) {
                int[] coordinates = rolitView.getClientConnection().getBoard().getCoordinates(i);
                rolitView.getClientConnection().makeMove(coordinates[0], coordinates[1]);
            }
        }
    }
}
