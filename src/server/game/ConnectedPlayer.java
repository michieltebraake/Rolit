package server.game;

import server.ServerPeer;

import java.net.Socket;

public class ConnectedPlayer {
    private String name;
    private Mark mark;
    private Socket socket;
    private ServerPeer peer;

    public ConnectedPlayer(String name, Mark mark, Socket socket, ServerPeer peer) {
        this.name = name;
        this.mark = mark;
        this.socket = socket;
        this.peer = peer;
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }

    public Socket getSocket() {
        return socket;
    }

    public ServerPeer getPeer() {
        return peer;
    }
}
