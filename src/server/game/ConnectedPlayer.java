package server.game;

import server.ServerPeer;
import util.Mark;

public class ConnectedPlayer {
    private String name;
    private Mark mark;
    private ServerPeer peer;

    public ConnectedPlayer(String name, Mark mark, ServerPeer peer) {
        this.name = name;
        this.mark = mark;
        this.peer = peer;
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }

    public ServerPeer getPeer() {
        return peer;
    }
}
