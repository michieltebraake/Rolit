package GUI;

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
        if(rolitView.getExitItem().equals(event.getSource())){
            rolitView.dispatchEvent(new WindowEvent(rolitView, WindowEvent.WINDOW_CLOSING));
        }

        JButton[] buttons = rolitView.getButtons();
        for (JButton button : buttons) {
            if (button.equals(event.getSource())) {
                button.setEnabled(false);
            }
        }
    }
}
