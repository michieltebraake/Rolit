package util.Peer;

import util.ProtocolHandler;

import java.io.*;
import java.net.Socket;

/**
 * Peer for a simple client-server application
 *
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Peer implements Runnable {
    protected Socket socket;
    protected BufferedReader inStream;
    protected BufferedWriter outStream;
    protected ProtocolHandler protocolHandler;
    protected String name;
    protected boolean keepGoing = true;

	/*@
       requires (nameArg != null) && (sockArg != null);
	 */

    /**
     * Constructor. creates a peer object based inStream the given parameters.
     *
     * @param socket Socket of the Peer-proces
     */
    public Peer(Socket socket, ProtocolHandler protocolHandler, String name) throws IOException {
        this.socket = socket;
        this.protocolHandler = protocolHandler;
        this.name = name;
        inStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        outStream = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Reads strings of the stream of the socket-connection and writes the characters to the default output
     */
    public void run() {
        while (keepGoing) {
            try {
                String message = inStream.readLine();
                System.out.println("["+ name + " | received] " + message);
                protocolHandler.handleMessage(message);
            } catch (IOException e) {
                shutDown();
            }
        }
    }

    public void send(String message) {
        try {
            System.out.println("["+ name + " | sent] " + message);
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
        System.out.println("[" + name + "] Closing socket");
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
