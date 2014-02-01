package util.Peer;

import server.RolitServer;
import server.connection.ServerConnection;
import util.ProtocolHandler;

import java.io.IOException;
import java.net.Socket;

public class ServerPeer extends Peer {
    private RolitServer rolitServer;

    /**
     * Constructor. creates a peer object based inStream the given parameters.
     *
     * @param socket          Socket of the Peer-proces
     * @param protocolHandler socketHandler
     * @param name            name of the peer
     */
    public ServerPeer(Socket socket, ProtocolHandler protocolHandler, String name, RolitServer rolitServer) throws IOException {
        super(socket, protocolHandler, name);
        this.rolitServer = rolitServer;
    }

    /**
     * Closes the connection, the sockets will be terminated
     */
    public void shutDown() {
        rolitServer.removeConnection((ServerConnection) protocolHandler);
        rolitServer.removeAuthenticated((ServerConnection) protocolHandler);
        super.shutDown();
    }
}
