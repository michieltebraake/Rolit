package server;

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
    List<ServerPeer> connections = new ArrayList<>();

    List<ConnectedPlayer> authenticatedPlayers = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Starting server...");
        new RolitServer().start();
    }

    private void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2727);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                ServerPeer peer = new ServerPeer(clientSocket);
                Thread thread = new Thread(peer);
                thread.start();

                connections.add(peer);

                if (connections.size() == 4) {
                    int i = 0;
                    Mark[] marks = Mark.getMarks(connections.size());
                    for (ServerPeer serverPeer : connections) {
                        authenticatedPlayers.add(new ConnectedPlayer("player" + i, marks[i], serverPeer));
                        i++;
                    }
                    Game game = new Game(authenticatedPlayers.toArray(new ConnectedPlayer[authenticatedPlayers.size()]));
                    for (ConnectedPlayer connectedPlayer : authenticatedPlayers){
                        connectedPlayer.getPeer().getServerConnection().setGame(game);
                    }
                    connections.clear(); //TODO Remove players from connections when they authenticate.
                    authenticatedPlayers.clear();
                    System.out.println("Starting game!");
                }
                //peer.handleTerminalInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
