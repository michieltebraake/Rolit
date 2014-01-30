package server;

import server.connection.ServerConnection;
import util.Protocol;

import java.io.*;
import java.net.Socket;

/**
 * Peer for a simple client-server application
 *
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class ServerPeer implements Runnable {
    protected String name;
    protected Socket socket;

    protected BufferedReader inStream;
    protected BufferedWriter outStream;

    private RolitServer rolitServer;
    private ServerConnection serverConnection;

    private boolean keepGoing = true;

	/*@
       requires (nameArg != null) && (sockArg != null);
	 */

    /**
     * Constructor. creates a peer object based on the given parameters.
     *
     * @param socket Socket of the Peer-proces
     */
    public ServerPeer(Socket socket, RolitServer rolitServer) throws IOException {
        this.socket = socket;
        this.rolitServer = rolitServer;

        inStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        outStream = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        System.out.println("Created socket!");
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    /**
     * Reads strings of the stream of the socket-connection and writes the characters to the default output
     */
    public void run() {
        while (keepGoing) {
            try {
                String message = inStream.readLine();
                System.out.println(message);
                serverConnection.handleMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String message) {
        System.out.println("Sending: " + message);
        try {
            outStream.write(message + "\n");
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Reads a string from the console and sends this string over the socket-connection to the Peer proces. On Peer.EXIT the method ends
     */
    public void handleTerminalInput() {
        try {
            String message = readString();
            if (message.equals(Protocol.EXIT)) {
                shutDown();
            } else {
                outStream.write("[ " + name + " ] " + message + "\n");
                outStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection, the sockets will be terminated
     */
    public void shutDown() {
        System.out.println("Closing socket!");
        try {
            //Make sure server doesn't expect client to keep playing.
            rolitServer.removeConnection(this);
            rolitServer.removeAuthenticated(this);

            keepGoing = false;
            inStream.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns name of the peer object
     */
    public String getName() {
        return name;
    }

    /**
     * read a line from the default input
     */
    static public String readString() {
        String antw = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            antw = in.readLine();
        } catch (IOException e) {
        }

        return (antw == null) ? "" : antw;
    }
}
