package client.GUI;

import client.Board;
import client.Connection.ClientConnection;
import client.Mark;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class RolitView extends JFrame implements Observer {
    private ButtonListener buttonListener;

    private JButton[] buttons = new JButton[64];

    private JMenuItem connectItem;
    private JMenuItem exitItem;
    private JMenuItem restartItem;

    private Board board;
    private ClientConnection clientConnection;

    public static void main(String[] args) {
        new RolitView();
    }

    /**
     * Constructor.
     * Sets up the main view with the game board.
     */
    public RolitView() {
        buttonListener = new ButtonListener(this);
        setupFrame();
    }

    public void setBoard(Board board) {
        this.board = board;
        buttonListener.setBoard(board);
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
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
     * @return exitItem the <code>JMenuItem</code> of the exit button.
     */
    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JMenuItem getConnectItem() {
        return connectItem;
    }

    public JMenuItem getRestartItem() {
        return restartItem;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setupProtocol(Socket socket) {
        clientConnection = new ClientConnection(socket, this);
    }

    /**
     * @return restartItem the <code>JMenuItem</code> of the restart buttons
     */

    private void setupFrame() {

        JPanel infoPanel = new JPanel();
        //infoPanel.setLayout(new BorderLayout(3, BorderLayout.EAST));

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
            rolitButton.setEnabled(false);
        }

        //Add menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        connectItem = new JMenuItem("Connect");
        connectItem.addActionListener(buttonListener);
        file.add(connectItem);
        restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(buttonListener);
        file.add(restartItem);
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
        for (int i = 0; i < buttons.length; i++) {
            Mark mark = board.getField(i);
            buttons[i].setBackground(mark.getColor());
            if (arg == false) {
                buttons[i].setEnabled(false);
            } else {
                if (mark != Mark.EMPTY) {
                    buttons[i].setEnabled(false);
                } else {
                    buttons[i].setEnabled(true);
                }
            }
        }
    }
}
