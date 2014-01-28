package client.Connection;

import client.Board;
import client.GUI.RolitView;
import client.HumanPlayer;
import client.Mark;
import client.Player;
import util.Protocol;
import util.ProtocolHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

public class ClientConnection extends Observable implements ProtocolHandler {
    private ClientPeer clientPeer;
    private Board board;
    private RolitView rolitView;

    private String username;
    private String pasaword;

    private int current = 0;

    public ClientConnection(Socket clientSocket, RolitView rolitView) {
        this.rolitView = rolitView;
        try {
            clientPeer = new ClientPeer(clientSocket, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(clientPeer);
        thread.start();
        joinGame();
    }

    public void joinGame() {
        clientPeer.send(Protocol.JOIN_GAME + " " + 3);
    }

    public void makeMove(int x, int y) {
        //Check if player has marks on the board & player can make a valid move.
        Mark mark = board.getPlayers()[current].getMark();
        List<Integer> rollFields = board.getRollFields(mark, x, y);

        if (!board.canMakeMove(mark) && board.nextToBall(x, y)) {
            clientPeer.send(Protocol.MAKE_MOVE + " " + board.getFieldID(x, y));
        } else if (!rollFields.isEmpty()) {
            clientPeer.send(Protocol.MAKE_MOVE + " " + board.getFieldID(x, y));
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
                        players[i] = new HumanPlayer(playerMark, playerName);
                    }
                    board = new Board(players);
                    rolitView.setBoard(board);
                    addObserver(rolitView);
                    setChanged();
                    notifyObservers(false);
                    break;
                case Protocol.REQUEST_MOVE:
                    setChanged();
                    notifyObservers(true);
                    break;
                case Protocol.BROADCAST_MOVE:
                    putMark(Integer.parseInt(args[0]), board.getPlayers()[current].getMark());
                    current++;
                    current = current % board.getPlayers().length;
                    setChanged();
                    notifyObservers(false);
                    break;
            }
        }
    }
}
