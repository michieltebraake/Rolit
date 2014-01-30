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

    public ServerConnection(Socket socket, RolitServer rolitServer, String name) {
        name = "Client No. " + name;
        this.rolitServer = rolitServer;
        try {
            peer = new ServerPeer(socket, this, name);
            Thread thread = new Thread(peer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    username = args[0];
                    peer.setName(username);
                    new AuthenticationConnection(this, username);
                    break;
                case Protocol.TOKEN_REPLY:
                    if (Crypto.verifyToken(Crypto.decodeBase64(args[0]), plainToken, publicKey)) {
                        peer.send(Protocol.AUTHENTICATED + " 0 0");
                        rolitServer.removeConnection(this);
                        //TODO ================ Client Authenticated ================
                        //wait for joingame?
                    } else {
                        //TODO false token handling.
                        peer.send(Protocol.ERROR + " Tokenreply was not valid! Disconnecting client.");
                        peer.shutDown();
                    }
                    break;
                case Protocol.JOIN_GAME:
                    rolitServer.joinWaitlist(this, Integer.parseInt(args[0]));
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
