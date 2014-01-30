package client.Connection;

import client.GUI.RolitView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;

public class Connect extends Thread {
    private RolitView rolitView;
    private String ip;
    private int port;
    private String username;
    private boolean ai;
    private int players;
    private PrivateKey privateKey;

    public Connect(RolitView rolitView, String ip, int port, String username, boolean ai, int players, PrivateKey privateKey) {
        this.rolitView = rolitView;
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.ai = ai;
        this.players = players;
        this.privateKey = privateKey;
    }

    public void run() {
        Socket socket;
        try {
            InetAddress.getByName(ip);
            socket = new Socket(ip, port);
            rolitView.setupProtocol(socket, username, ai, players, privateKey);
        } catch (UnknownHostException e) {
            rolitView.getStatusLabel().setText("Error: Connection with the game server could not be established. (Unknown Host)");
        } catch (IOException e) {
            rolitView.getStatusLabel().setText("Error: Connection with the game server could not be established. (Connection Refused)");
        }
    }
}