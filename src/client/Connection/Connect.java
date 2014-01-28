package client.Connection;

import client.GUI.RolitView;

import java.io.IOException;
import java.net.Socket;

public class Connect extends Thread {
    private RolitView rolitView;
    private String ip;
    private int port;

    public Connect(RolitView rolitView, String ip, int port) {
        this.rolitView = rolitView;
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        Socket socket;
        try {
            socket = new Socket(ip, port);
            rolitView.setupProtocol(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}