package client.GUI;

import client.Board;
import client.Connection.ClientConnection;
import util.Mark;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class RolitView extends JFrame implements Observer {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8820391706032092419L;

	private ButtonListener buttonListener;

    private JButton[] buttons = new JButton[Board.DIM * Board.DIM];

    private JMenuItem connectItem;
    private JMenuItem exitItem;
    private JMenuItem restartItem;

    private JLabel statusLabel;
    private JLabel gameLabel;

    private JMenuItem showPossibleMoves;
    private JMenuItem makeAIMove;
    private JMenuItem switchToAI;

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

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getGameLabel() {
        return gameLabel;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setupProtocol(Socket socket, String username, boolean ai, int players, PrivateKey privateKey) {
        statusLabel.setText("Connected!");
        clientConnection = new ClientConnection(socket, this, username, ai, players, privateKey);
    }

    public void closeProtocol() {
        statusLabel.setText("Not connected.");
        clientConnection = null;
    }

    /**
     * @return restartItem the <code>JMenuItem</code> of the restart buttons
     */

    private void setupFrame() {
        //Add buttons
        JPanel buttonPanel = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 800);
        add(buttonPanel);

        buttonPanel.setLayout(new GridLayout(Board.DIM, Board.DIM));
        for (int i = 0; i < Board.DIM * Board.DIM; i++) {
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


        JMenu cheats = new JMenu("Cheats");
        showPossibleMoves = new JMenuItem("Show possible moves");
        showPossibleMoves.addActionListener(buttonListener);
        cheats.add(showPossibleMoves);
        makeAIMove = new JMenuItem("Make AI move");
        makeAIMove.addActionListener(buttonListener);
        cheats.add(makeAIMove);
        switchToAI = new JMenuItem("Switch to AI");
        switchToAI.addActionListener(buttonListener);
        cheats.add(switchToAI);

        menuBar.add(file);
        menuBar.add(cheats);

        //Add status bar at the bottom
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
        statusPanel.setLayout(new BorderLayout());
        //statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Not connected.");
        statusPanel.add(statusLabel, BorderLayout.WEST);
        gameLabel = new JLabel("Not in a game.");
        statusPanel.add(gameLabel, BorderLayout.EAST);

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
        if (observable instanceof ClientConnection) {
            ClientConnection clientConnection = (ClientConnection) observable;
            for (int i = 0; i < buttons.length; i++) {
                Mark mark = clientConnection.getBoard().getField(i);
                buttons[i].setBackground(mark.getColor());
                //Check if client is AI (buttons should always be disabled for
                if (clientConnection.getAI() || (boolean) arg == false) {
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
}
