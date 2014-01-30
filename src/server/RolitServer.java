package server;

import server.game.ConnectedPlayer;
import server.connection.ServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RolitServer {
    List<ServerPeer> connections = new ArrayList<>();

    List<ServerPeer> authenticated2 = new ArrayList<>();
    List<ServerPeer> authenticated3 = new ArrayList<>();
    List<ServerPeer> authenticated4 = new ArrayList<>();

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
                ServerPeer peer = new ServerPeer(clientSocket, this);
                Thread thread = new Thread(peer);
                thread.start();

                ServerConnection serverConnection = new ServerConnection(clientSocket, String.valueOf(clientNo));
                clientNo++;
                connections.add(peer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeConnection(ServerPeer serverPeer) {
        connections.remove(serverPeer);
    }

    public void removeAuthenticated(ServerPeer serverPeer) {
        authenticated2.remove(serverPeer);
        authenticated3.remove(serverPeer);
        authenticated4.remove(serverPeer);
    }

    public boolean isConnected(String username) {
        return isConnected(username, connections) + isConnected(username, authenticated2) + isConnected(username, authenticated3) + isConnected(username, authenticated4) > 1;
    }

    private int isConnected(String username, List<ServerPeer> connected) {
        int connections = 0;
        for (ServerPeer serverPeer : connected) {
            if (serverPeer.getServerConnection().getUsername().equals(username)) {
                connections++;
            }
        }
        return connections;
    }

    public void joinWaitlist(ServerPeer serverPeer, int players) {
        switch (players) {
            case 2:
                authenticated2.add(serverPeer);
                if (authenticated2.size() == 2) {
                    startGame(authenticated2);
                }
                break;
            case 3:
                authenticated3.add(serverPeer);
                if (authenticated3.size() == 3) {
                    startGame(authenticated3);
                }
                break;
            case 4:
                authenticated4.add(serverPeer);
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
    private void startGame(List<ServerPeer> authenticated) {
        Mark[] marks = Mark.getMarks(authenticated.size());
        ConnectedPlayer[] players = new ConnectedPlayer[authenticated.size()];
        for (int i = 0; i < authenticated.size(); i++) {
            System.out.println("Adding player to start list");
            players[i] = new ConnectedPlayer(authenticated.get(i).getServerConnection().getUsername(), marks[i], authenticated.get(i));
        }

        Game game = new Game(players);
        for (ConnectedPlayer connectedPlayer : players) {
            connectedPlayer.getPeer().getServerConnection().setGame(game);
        }
        authenticated.clear();
        System.out.println("Starting game!");
    }
}
