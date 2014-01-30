package server.game;

import server.connection.ServerConnection;
import util.Mark;

public class ConnectedPlayer {
    private String name;
    private Mark mark;
    private ServerConnection serverConnection;

    public ConnectedPlayer(String name, Mark mark, ServerConnection serverConnection) {
        this.name = name;
        this.mark = mark;
        this.serverConnection = serverConnection;
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}
