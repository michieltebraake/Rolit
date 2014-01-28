package client.Connection;

import client.Board;
import client.ComputerPlayer;
import client.GUI.RolitView;
import client.Mark;
import client.Player;
import client.Strategy.SmartStrategy;
import util.Protocol;
import util.ProtocolHandler;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

public class ClientConnection extends Observable implements ProtocolHandler {
    private ClientPeer peer;
    private Board board;
    private RolitView rolitView;
    private int current = 0;

    private String username;
    private boolean ai;

    public ClientConnection(Socket socket, RolitView rolitView, String username, String password, boolean ai) {
        this.rolitView = rolitView;

        this.username = username;
        this.ai = ai;

        try {
            peer = new ClientPeer(socket, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(peer);
        thread.start();
        joinGame();
    }

    public Board getBoard() {
        return board;
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
                case Protocol.START_GAME:
                    Player[] players = new Player[args.length];
                    for (int i = 0; i < args.length; i++) {
                        String playerName = args[i];
                        Mark playerMark = Mark.values()[i + 1];
                        //players[i] = new HumanPlayer(playerMark, playerName);
                        players[i] = new ComputerPlayer(playerMark, playerName, new SmartStrategy());
                    }
                    board = new Board(players);
                    addObserver(rolitView);
                    setChanged();
                    notifyObservers(false);
                    break;
                case Protocol.REQUEST_MOVE:
                    setChanged();
                    notifyObservers(true);
                    if (board.getPlayers()[current] instanceof ComputerPlayer) {
                        int[] coordinates = board.getCoordinates(board.getPlayers()[current].determineMove(board));
                        rolitView.getClientConnection().makeMove(coordinates[0], coordinates[1]);
                    }
                    break;
                case Protocol.BROADCAST_MOVE:
                    putMark(Integer.parseInt(args[0]), board.getPlayers()[current].getMark());
                    current++;
                    current = current % board.getPlayers().length;
                    setChanged();
                    notifyObservers(false);
                    break;
                case Protocol.GAME_OVER:
                    setChanged();
                    notifyObservers(false);
                    JOptionPane.showMessageDialog(rolitView, board.getWinner().toString() + " has won the game!", "Game over!", JOptionPane.INFORMATION_MESSAGE);
                    rolitView.closeProtocol();
                    peer.send(Protocol.EXIT);
                    peer.shutDown();
                    break;
            }
        }
    }
}
