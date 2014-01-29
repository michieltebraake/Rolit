package client.Connection;

import util.ProtocolHandler;

import java.io.*;
import java.net.Socket;

/**
 * Peer for a simple client-server application
 *
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class ClientPeer implements Runnable {
    protected String name;
    protected Socket socket;

    protected BufferedReader inStream;
    protected BufferedWriter outStream;

    private ProtocolHandler protocolHandler;

    private boolean keepGoing = true;

	/*@
       requires (nameArg != null) && (sockArg != null);
	 */

    /**
     * Constructor. creates a peer object based inStream the given parameters.
     *
     * @param socket Socket of the Peer-proces
     */
    public ClientPeer(Socket socket, ProtocolHandler protocolHandler) throws IOException {
        this.socket = socket;
        this.protocolHandler = protocolHandler;

        inStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        outStream = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    /**
     * Reads strings of the stream of the socket-connection and writes the characters to the default output
     */
    public void run() {
        while (keepGoing) {
            try {
                String message = inStream.readLine();
                if (message != null) {
                    System.out.println("Received: " + message);
                    protocolHandler.handleMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String message) {
        try {
            System.out.println("Sent: " + message);
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
            if (message.equals("exit")) {
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
