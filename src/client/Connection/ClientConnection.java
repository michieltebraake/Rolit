package client.Connection;

import client.*;
import client.GUI.RolitView;
import client.Strategy.SmartStrategy;
import util.*;
import util.Peer.Peer;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.List;
import java.util.Observable;

public class ClientConnection extends Observable implements ProtocolHandler {
    private Peer peer;
    private Board board;
    private RolitView rolitView;
    private int current = 0;
    private String username;
    private boolean ai;
    private PrivateKey privateKey;

    public ClientConnection(Socket socket, RolitView rolitView, String username, boolean ai, PrivateKey privateKey) {
        this.rolitView = rolitView;
        this.username = username;
        this.ai = ai;
        this.privateKey = privateKey;
        try {
            peer = new Peer(socket, this, "Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(peer);
        thread.start();
        authenticate();
    }

    public Board getBoard() {
        return board;
    }

    public boolean getAI() {
        return ai;
    }

    public void authenticate() {
        peer.send(Protocol.AUTHENTICATE_CLIENT + " " + username);
    }

    public void joinGame() {
        peer.send(Protocol.JOIN_GAME + " " + 3);
    }

    public void makeMove(int x, int y) {
        //Check if player has marks on the board & player can make a valid move.
        Mark mark = board.getPlayers()[current].getMark();
        List<Integer> rollFields = board.getRollFields(mark, x, y);

        if (!board.canMakeMove(mark) && board.nextToBall(x, y)) {
            peer.send(Protocol.MAKE_MOVE + " " + board.getFieldID(x, y));
        } else if (!rollFields.isEmpty()) {
            peer.send(Protocol.MAKE_MOVE + " " + board.getFieldID(x, y));
        }
    }

    private void putMark(int fieldID, Mark mark) {
        int[] coordinates = board.getCoordinates(fieldID);
        int x = coordinates[0];
        int y = coordinates[1];
        List<Integer> rollFields = board.getRollFields(mark, x, y);
        if (!board.canMakeMove(mark) && board.nextToBall(x, y)) {
            board.setField(x, y, mark);
        } else if (!rollFields.isEmpty()) {
            board.setField(x, y, mark);
            for (int field : rollFields) {
                board.setField(field, mark);
            }
        }
    }

    public void handleMessage(String message) {
        String[] messageSplit = message.split(" ");
        if (messageSplit.length > 0) {

            String protocolMessage = messageSplit[0];
            //Make a new array of arguments that doesn't contain the protocol word.
            String args[] = new String[messageSplit.length - 1];
            for (int i = 0; i < messageSplit.length - 1; i++) {
                args[i] = messageSplit[i + 1];
            }

            switch (protocolMessage) {
                case Protocol.TOKEN_REQUEST:
                    String base64Token = Crypto.encodeBase64(Crypto.signMessage(args[0], privateKey));
                    peer.send(Protocol.TOKEN_REPLY + " " + base64Token);
                    break;
                case Protocol.AUTHENTICATED:
                    //TODO ================ Client authenticated ================
                    break;
                case Protocol.START_GAME:
                    Mark[] marks = Mark.getMarks(args.length);
                    Player[] players = new Player[args.length];
                    for (int i = 0; i < args.length; i++) {
                        // Check via username and only create computerplayer if player chose ai and username equals players name
                        if (ai) {
                            players[i] = new ComputerPlayer(marks[i], args[i], new SmartStrategy());
                        } else {
                            players[i] = new HumanPlayer(marks[i], args[i]);
                        }
                    }
                    board = new Board(players);
                    addObserver(rolitView);
                    setChanged();
                    notifyObservers(false);
                    rolitView.getGameLabel().setText("In game!");
                    break;
                case Protocol.REQUEST_MOVE:
                    setChanged();
                    notifyObservers(true);
                    if (board.getPlayers()[current] instanceof ComputerPlayer) {
                        /*try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        int[] coordinates = board.getCoordinates(board.getPlayers()[current].determineMove(board));
                        rolitView.getClientConnection().makeMove(coordinates[0], coordinates[1]);
                    }
                    rolitView.getGameLabel().setText("It is your turn!");
                    break;
                case Protocol.BROADCAST_MOVE:
                    putMark(Integer.parseInt(args[0]), board.getPlayers()[current].getMark());
                    current++;
                    current = current % board.getPlayers().length;
                    setChanged();
                    notifyObservers(false);
                    rolitView.getGameLabel().setText("It is " + board.getPlayers()[current].getMark() + "'s turn!");
                    break;
                case Protocol.GAME_OVER:
                    setChanged();
                    notifyObservers(false);
                    JOptionPane.showMessageDialog(rolitView, board.getWinner().toString() + " has won the game!", "Game over!", JOptionPane.INFORMATION_MESSAGE);
                    rolitView.closeProtocol();
                    peer.send(Protocol.EXIT);
                    peer.shutDown();
                    rolitView.getGameLabel().setText("Game over! " + board.getWinner().toString() + " has won.");
                    break;
            }
        }
    }
}
