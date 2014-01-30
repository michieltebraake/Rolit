package client.Connection;

import client.GUI.ConnectView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class AuthConnect extends Thread {
    private ConnectView connectView;
    public final static String AUTH_SERVER = "ss-security.student.utwente.nl";
    public final static int AUTH_PORT = 2013;

    public AuthConnect(ConnectView connectView) {
        this.connectView = connectView;
    }

    public void run() {
        Socket clientSocket;
        try {
            InetAddress.getByName(AUTH_SERVER);
            clientSocket = new Socket(AUTH_SERVER, AUTH_PORT);
            connectView.setupAuthentication(clientSocket);
        } catch (UnknownHostException e) {
            connectView.getRolitView().getStatusLabel().setText("Error: Connection with the authentication server could not be established. (Unknown Host)");
        } catch (IOException e) {
            connectView.getRolitView().getStatusLabel().setText("Error: Connection with the authentication server could not be established. (Connection Refused)");
        }
    }
}