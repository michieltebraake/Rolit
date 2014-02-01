package server.game;

import server.connection.ServerConnection;
import util.Board;
import util.Mark;
import util.Player;

public class ConnectedPlayer extends Player {
    private ServerConnection serverConnection;

    public ConnectedPlayer(String name, Mark mark, ServerConnection serverConnection) {
        super(name, mark);
        this.serverConnection = serverConnection;
    }

    @Override
    public int determineMove(Board board) {
        return 0;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}
