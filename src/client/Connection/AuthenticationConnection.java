package client.Connection;

import client.GUI.ConnectView;
import util.Crypto;
import util.Peer.Peer;
import util.ProtocolHandler;

import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;

public class AuthenticationConnection implements ProtocolHandler {
    private Peer peer;
    private ConnectView connectView;
    private PrivateKey privateKey;


    public AuthenticationConnection(Socket socket, ConnectView connectView, String username, String password) {
        this.connectView = connectView;
        try {
            peer = new Peer(socket, this, "Authentication");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(peer);
        thread.start();
        peer.send("IDPLAYER " + username + " " + password);
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
                case "PRIVKEY":
                    privateKey = Crypto.decodePrivateKey(Crypto.decodeBase64(args[0]));
                    connectView.createClientConnection(privateKey);
                    peer.shutDown();
                    break;
                default:
                    peer.shutDown();
                    //TODO: GUI: wrong pass/username.
                    break;

            }
        }
    }
}