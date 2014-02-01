package server;

import server.connection.ServerConnection;
import server.game.ConnectedPlayer;
import server.game.Game;
import util.Mark;
import util.Peer.Peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RolitServer {
    private List<ServerConnection> connections = new ArrayList<>();

    private List<ServerConnection> authenticated2 = new ArrayList<>();
    private List<ServerConnection> authenticated3 = new ArrayList<>();
    private List<ServerConnection> authenticated4 = new ArrayList<>();
    
    private ServerSocket serverSocket = null;

    public static void main(String[] args) {
        new RolitServer().start();
    }

    public RolitServer(){
    }

    private void start() {
        boolean startedServer = false;
        while (!startedServer) {
            System.out.print("Port: ");
            String portString = Peer.readString();

            try {
                int port = Integer.parseInt(portString);

                try {
                    serverSocket = new ServerSocket(port);
                    startedServer = true;
                } catch (IOException e) {
                    System.out.println("Unable to create server socket! Perhaps that port is already in use?");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid port number!");
            }
        }
        
        System.out.println("Starting server...");

        int clientNo = 1;
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

    /**
     * Removes a player connection from the connections list.
     *
     * @param serverConnection connection to remove
     */
    public void removeConnection(ServerConnection serverConnection) {
        connections.remove(serverConnection);
    }

    /**
     * Removes a player connection from all waitlists.
     *
     * @param serverConnection connection to remove
     */
    public void removeAuthenticated(ServerConnection serverConnection) {
        authenticated2.remove(serverConnection);
        authenticated3.remove(serverConnection);
        authenticated4.remove(serverConnection);
    }

    /**
     * Checks if a player is connected (waiting in the not authenticated list, or in any waitlist for 2-4 player games)
     *
     * @param username name of user
     * @return true if player is connected
     */
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

    /**
     * Makes a player join the waitlist for a game.
     * Starts a game if enough players are in the waitlist.
     *
     * @param serverConnection connection to join the waitlist with
     * @param players number of players to play with
     */
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
