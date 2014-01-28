package client.Connection;

import client.GUI.RolitView;

import java.io.IOException;
import java.net.Socket;

public class Connect extends Thread {
    private RolitView rolitView;
    private String ip;
    private int port;

    private String username;
    private String password;
    private boolean ai;

    public Connect(RolitView rolitView, String ip, int port, String username, String password, boolean ai) {
        this.rolitView = rolitView;
        this.ip = ip;
        this.port = port;

        this.username = username;
        this.password = password;
        this.ai = ai;
    }

    public void run() {
        Socket socket;
        try {
            socket = new Socket(ip, port);
            rolitView.setupProtocol(socket, username, password, ai);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}