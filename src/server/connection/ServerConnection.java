package server.connection;


import server.RolitServer;
import server.game.ConnectedPlayer;
import server.game.Game;
import util.Crypto;
import util.Peer.ServerPeer;
import util.Protocol;
import util.ProtocolHandler;

import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

public class ServerConnection implements ProtocolHandler {
    private ServerPeer peer;
    private RolitServer rolitServer;

    private Game game;
    private PublicKey publicKey;
    private String plainToken;

    private String username;
    private boolean authenticated;

    /**
     * Constructs a ServerConnection.
     *
     * @param socket socket connection with the player
     * @param rolitServer rolitServer instance
     * @param name name of client connection
     */
    public ServerConnection(Socket socket, RolitServer rolitServer, String name) {
        name = "Client No. " + name;
        this.rolitServer = rolitServer;
        try {
            peer = new ServerPeer(socket, this, name, rolitServer);
            Thread thread = new Thread(peer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return username username of player
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return peer peer connection with the client
     */
    public ServerPeer getPeer(){
        return peer;
    }

    /**
     * @param game game player is in
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * @param publicKey public key of player
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Sends a token request to the client.
     */
    public void requestToken() {
        plainToken = Crypto.generateToken();
        peer.send(Protocol.TOKEN_REQUEST + " " + plainToken);
    }

    /**
     * Sends a start game message with the names of the players in the game to the client.
     *
     * @param players players in the game
     */
    public void startGame(ConnectedPlayer[] players) {
        StringBuilder message = new StringBuilder();
        for (ConnectedPlayer player : players) {
            message.append(player.getName());
            message.append(" ");
        }
        peer.send(Protocol.START_GAME + " " + message.toString());
    }

    /**
     * Sends a request move message to the client.
     * This informs the client that he needs to make a move.
     */
    public void requestMove() {
        peer.send(Protocol.REQUEST_MOVE);
    }

    /**
     * Sends a broadcast move message to the client.
     * This informs the client of a move that has been made.
     *
     * @param field id of the field where a mark was placed
     */
    public void broadcastMove(int field) {
        peer.send(Protocol.BROADCAST_MOVE + " " + field);
    }

    /**
     * Sends a game over message to the client.
     * This informs the client that the game has ended.
     */
    public void gameOver() {
        peer.send(Protocol.GAME_OVER);
    }

    /**
     * Handles a message that was received from the client.
     *
     * @param message String of the message that was received
     */
    public void handleMessage(String message) {
        String[] messageSplit = message.split(" ");
        if (messageSplit.length > 0) {

            String protocolMessage = messageSplit[0];
            //Make a new array of arguments that doesn't contain the protocol word.
            String args[] = new String[messageSplit.length - 1];

            System.arraycopy(messageSplit, 1, args, 0, messageSplit.length - 1);

            if (!authenticated) {
                switch (protocolMessage) {
                    case Protocol.AUTHENTICATE_CLIENT:
                        username = args[0];
                        if (!rolitServer.isConnected(username)) {
                            peer.setName(username);
                            new AuthenticationConnection(this, username);
                        } else {
                            peer.send(Protocol.ERROR + " User is already connected! Disconnecting client.");
                            peer.shutDown();
                        }
                        break;
                    case Protocol.TOKEN_REPLY:
                        if (Crypto.verifyToken(Crypto.decodeBase64(args[0]), plainToken, publicKey)) {
                            authenticated = true;
                            peer.send(Protocol.AUTHENTICATED + " 0 0");
                            rolitServer.removeConnection(this);
                        } else {
                            peer.send(Protocol.ERROR + " Tokenreply was not valid! Disconnecting client.");
                            peer.shutDown();
                        }
                        break;
                    default:
                        peer.send(Protocol.ERROR + " Invalid protocol message! User is not yet authenticated, disconnecting client.");
                        peer.shutDown();
                        break;
                }
            } else {
                switch (protocolMessage) {
                    case Protocol.JOIN_GAME:
                        rolitServer.joinWaitlist(this, Integer.parseInt(args[0]));
                        break;
                    case Protocol.MAKE_MOVE:
                        if (game.getBoard().getPlayers()[game.getCurrent()].getServerConnection() == this) {
                            int field = Integer.parseInt(args[0]);
                            game.takeTurn(field);
                        } else {
                            peer.send(Protocol.ERROR + " It is not your turn!");
                        }
                        break;
                    case Protocol.EXIT:
                        peer.shutDown();
                        break;
                }
            }
        }
    }
}
