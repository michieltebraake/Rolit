package client.Connection;

import client.GUI.RolitView;

import java.io.IOException;
import java.net.Socket;

public class Connect extends Thread {
    private RolitView rolitView;

    public Connect(RolitView rolitView) {
        this.rolitView = rolitView;
    }

    public void run() {
        Socket socket;
        try {
            //socket = new Socket("130.89.227.122", 2727);
            socket = new Socket("localhost", 2727);
            ClientPeer peer = new ClientPeer(socket, rolitView);
            Thread thread = new Thread(peer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}