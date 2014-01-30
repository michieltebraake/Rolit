package server;

import server.connection.ServerConnection;
import server.game.ConnectedPlayer;
import server.game.Game;
import util.Mark;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RolitServer {
    List<ServerConnection> connections = new ArrayList<>();

    List<ServerConnection> authenticated2 = new ArrayList<>();
    List<ServerConnection> authenticated3 = new ArrayList<>();
    List<ServerConnection> authenticated4 = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Starting server...");
        new RolitServer().start();
    }

    private void start() {
        ServerSocket serverSocket = null;
        int clientNo = 1;
        try {
            serverSocket = new ServerSocket(2727);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(clientSocket, this, String.valueOf(clientNo));

                clientNo++;
                connections.add(serverConnection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeConnection(ServerConnection serverConnection) {
        connections.remove(serverConnection);
    }

    public void removeAuthenticated(ServerConnection serverConnection) {
        authenticated2.remove(serverConnection);
        authenticated3.remove(serverConnection);
        authenticated4.remove(serverConnection);
    }

    public boolean isConnected(String username) {
        return isConnected(username, connections) + isConnected(username, authenticated2) + isConnected(username, authenticated3) + isConnected(username, authenticated4) > 1;
    }

    private int isConnected(String username, List<ServerConnection> connected) {
        int connections = 0;
        for (ServerConnection serverConnection : connected) {
            if (serverConnection.getUsername().equals(username)) {
                connections++;
            }
        }
        return connections;
    }

    public void joinWaitlist(ServerConnection serverConnection, int players) {
        switch (players) {
            case 2:
                authenticated2.add(serverConnection);
                if (authenticated2.size() == 2) {
                    startGame(authenticated2);
                }
                break;
            case 3:
                authenticated3.add(serverConnection);
                if (authenticated3.size() == 3) {
                    startGame(authenticated3);
                }
                break;
            case 4:
                authenticated4.add(serverConnection);
                if (authenticated4.size() == 4) {
                    startGame(authenticated4);
                }
                break;
        }
    }

    /**
     * Starts a game with given players.
     * The given list will be cleared after starting a game, this means the peer is no longer in any list.
     *
     * @param authenticated List of players to start a game with
     */
    private void startGame(List<ServerConnection> authenticated) {
        Mark[] marks = Mark.getMarks(authenticated.size());
        ConnectedPlayer[] players = new ConnectedPlayer[authenticated.size()];
        for (int i = 0; i < authenticated.size(); i++) {
            players[i] = new ConnectedPlayer(authenticated.get(i).getUsername(), marks[i], authenticated.get(i));
        }

        Game game = new Game(players);
        for (ConnectedPlayer connectedPlayer : players) {
            connectedPlayer.getServerConnection().setGame(game);
        }
        authenticated.clear();
        System.out.println("Starting game!");
    }
}
