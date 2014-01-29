package server.game;

import server.RolitServer;
import util.Crypto;
import util.Protocol;
import server.ServerPeer;

import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

public class ServerConnection {
    private RolitServer rolitServer;
    private ServerPeer peer;

    private Game game;
    public final static String AUTH_SERVER = "130.89.163.155";
    public final static int AUTH_PORT = 2013;

    private PublicKey publicKey;
    private String plainToken;

    private String username;

    public ServerConnection(ServerPeer peer, RolitServer rolitServer) {
        this.peer = peer;
        this.rolitServer = rolitServer;
    }

    public String getUsername() {
        return username;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void requestToken() {
        plainToken = Crypto.generateToken();
        peer.send(Protocol.TOKEN_REQUEST + " " + plainToken);
    }

    public void startGame(ConnectedPlayer[] players) {
        StringBuilder message = new StringBuilder();
        for (ConnectedPlayer player : players) {
            System.out.println(player.getName());
            message.append(player.getName());
            message.append(" ");
        }
        peer.send(Protocol.START_GAME + " " + message.toString());
    }

    public void requestMove() {
        peer.send(Protocol.REQUEST_MOVE);
    }

    public void broadcastMove(int field) {
        peer.send(Protocol.BROADCAST_MOVE + " " + field);
    }

    public void gameOver() {
        peer.send(Protocol.GAME_OVER);
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
                case Protocol.AUTHENTICATE_CLIENT:
                    try {
                        username = args[0];
                        if (!rolitServer.isConnected(username)) {
                            Socket socket = new Socket(AUTH_SERVER, AUTH_PORT);
                            AuthenticationPeer authenticationPeer = new AuthenticationPeer(socket, this);
                            Thread thread = new Thread(authenticationPeer);
                            thread.start();
                            authenticationPeer.send("PUBLICKEY " + username);
                        } else {
                            peer.send(Protocol.ERROR + " User is already connected! Disconnecting client.");
                            peer.shutDown();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Protocol.TOKEN_REPLY:
                    if (Crypto.verifyToken(Crypto.decodeBase64(args[0]), plainToken, publicKey)) {
                        peer.send(Protocol.AUTHENTICATED + " 0 0");
                        rolitServer.removeConnection(peer);
                        //TODO ================ Client Authenticated ================
                        //wait for joingame?
                    } else {
                        //TODO false token handling.
                        peer.send(Protocol.ERROR + " Tokenreply was not valid! Disconnecting client.");
                        peer.shutDown();
                    }
                    System.out.println("========> Received base64: " + args[0]);
                    break;
                case Protocol.JOIN_GAME:
                    rolitServer.joinWaitlist(peer, Integer.parseInt(args[0]));
                    break;
                case Protocol.MAKE_MOVE:
                    int field = Integer.parseInt(args[0]);
                    game.takeTurn(field);
                    break;
                case Protocol.EXIT:
                    peer.shutDown();
                    break;
            }
        }
    }
}
