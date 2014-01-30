package util.Peer;

import server.connection.ServerConnection;
import server.game.Game;

import java.io.IOException;
import java.net.Socket;

public class ServerPeer extends Peer {
    private ServerConnection serverConnection;
    private Game game;

    /**
     * Constructor. creates a peer object based inStream the given parameters.
     *
     * @param socket          Socket of the Peer-proces
     * @param serverConnection
     * @param name
     */
    public ServerPeer(Socket socket, ServerConnection serverConnection, String name) throws IOException {
        super(socket, serverConnection, name);
        this.serverConnection = serverConnection;
    }

    public ServerConnection getServerConnection() {
        return  serverConnection;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Closes the connection, the sockets will be terminated
     */
    public void shutDown() {
        //TODO; remove from server connections etc.
        super.shutDown();
    }
}
