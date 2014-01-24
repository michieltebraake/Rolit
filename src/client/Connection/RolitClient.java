package client.Connection;

import server.Protocol;

import java.io.IOException;
import java.net.Socket;

public class RolitClient {


    public static void main(String[] args) {
        new RolitClient().start();
    }

    private void start(){
        Socket socket = null;
        try {
            socket = new Socket("localhost", 2727);
            ClientPeer peer = new ClientPeer(socket);
            Thread thread = new Thread(peer);
            thread.start();
            //peer.handleTerminalInput();
            peer.send(Protocol.JOIN_GAME);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
