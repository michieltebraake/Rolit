package client.Connection;

import client.GUI.ConnectView;
import util.AuthenticationProtocol;
import util.Crypto;
import util.Peer.Peer;
import util.ProtocolHandler;

import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;

public class AuthenticationConnection implements ProtocolHandler {
    private Peer peer;
    private ConnectView connectView;


    public AuthenticationConnection(Socket socket, ConnectView connectView, String username, String password) {
        this.connectView = connectView;
        try {
            peer = new Peer(socket, this, "Authentication");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(peer);
        thread.start();
        peer.send(AuthenticationProtocol.REQUEST_PRIVATEKEY + " " + username + " " + password);
    }


    public void handleMessage(String message) {
        String[] messageSplit = message.split(" ");
        if (messageSplit.length > 0) {

            String protocolMessage = messageSplit[0];
            //Make a new array of arguments that doesn't contain the protocol word.
            String args[] = new String[messageSplit.length - 1];

            System.arraycopy(messageSplit, 1, args, 0, messageSplit.length - 1);

            switch (protocolMessage) {
                case AuthenticationProtocol.PRIVATEKEY_RESPONSE:
                    PrivateKey privateKey = Crypto.decodePrivateKey(Crypto.decodeBase64(args[0]));
                    connectView.createClientConnection(privateKey);
                    peer.shutDown();
                    break;
                case AuthenticationProtocol.ERROR:
                    connectView.getRolitView().getStatusLabel().setText("Authentication failed (Wrong password/username)! Not connected.");
                    peer.shutDown();
                    break;
                default:
                    peer.shutDown();
                    break;

            }
        }
    }
}