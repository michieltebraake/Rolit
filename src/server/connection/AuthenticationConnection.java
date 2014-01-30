package server.connection;

import util.AuthenticationProtocol;
import util.Crypto;
import util.Peer.Peer;
import util.Protocol;
import util.ProtocolHandler;

import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

public class AuthenticationConnection implements ProtocolHandler{
    private ServerConnection serverConnection;
    private Peer peer;

    public final static String AUTH_SERVER = "130.89.163.155";
    public final static int AUTH_PORT = 2013;

    public AuthenticationConnection(ServerConnection serverConnection, String username){
        this.serverConnection = serverConnection;
        Socket socket;
        try {
            socket = new Socket(AUTH_SERVER, AUTH_PORT);
            peer = new Peer(socket, this, "Auth " + username);
            Thread thread = new Thread(peer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        peer.send(AuthenticationProtocol.REQUEST_PUBLICKEY + " " + username);
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
                case AuthenticationProtocol.PUBLICKEY_RESPONSE:
                    PublicKey publicKey = Crypto.decodePublicKey(Crypto.decodeBase64(args[0]));
                    serverConnection.setPublicKey(publicKey);
                    serverConnection.requestToken();
                    peer.shutDown();
                    break;
                default:
                    serverConnection.getPeer().send(Protocol.ERROR + " Authentication server could not identify you!");
                    serverConnection.getPeer().shutDown();
                    peer.shutDown();
            }
        }
    }
}
