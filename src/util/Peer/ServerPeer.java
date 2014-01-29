package util.Peer;

import util.Peer.Peer;
import util.ProtocolHandler;

import java.io.IOException;
import java.net.Socket;

public class ServerPeer extends Peer {

    /**
     * Constructor. creates a peer object based inStream the given parameters.
     *
     * @param socket          Socket of the Peer-proces
     * @param protocolHandler
     * @param name
     */
    public ServerPeer(Socket socket, ProtocolHandler protocolHandler, String name) throws IOException {
        super(socket, protocolHandler, name);
    }

    /**
     * Closes the connection, the sockets will be terminated
     */
    public void shutDown() {
        //TODO; remove from server connections etc.
        super.shutDown();
    }
}
