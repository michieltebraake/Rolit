package client.Connection;

import client.GUI.RolitView;

import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;

public class Connect extends Thread {
    private RolitView rolitView;
    private String ip;
    private int port;
    private String username;
    private boolean ai;
    private PrivateKey privateKey;

    public Connect(RolitView rolitView, String ip, int port, String username, boolean ai, PrivateKey privateKey) {
        this.rolitView = rolitView;
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.ai = ai;
        this.privateKey = privateKey;
    }

    public void run() {
        Socket socket;
        try {
            socket = new Socket(ip, port);
            rolitView.setupProtocol(socket, username, ai, privateKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}