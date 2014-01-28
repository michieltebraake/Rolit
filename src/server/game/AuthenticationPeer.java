package server.game;

import util.Crypto;
import util.ProtocolHandler;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

/**
 * Peer for a simple client-server application
 *
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class AuthenticationPeer implements Runnable {
    protected String name;
    protected Socket socket;

    protected BufferedReader inStream;
    protected BufferedWriter outStream;

    private ServerConnection serverConnection;
    private boolean keepGoing = true;

	/*@
       requires (nameArg != null) && (sockArg != null);
	 */

    /**
     * Constructor. creates a peer object based inStream the given parameters.
     *
     * @param socket Socket of the Peer-proces
     */
    public AuthenticationPeer(Socket socket, ServerConnection serverConnection) throws IOException {
        this.socket = socket;
        this.serverConnection = serverConnection;
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
                System.out.println("[AUTH] Received: " + message);
                handleMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String message) {
        try {
            System.out.println("[AUTH] Sent: " + message);
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

    public void handleMessage(String message) {
        String[] messageSplit = message.split(" ");
        if (messageSplit.length > 0) {

            String protocolMessage = messageSplit[0];
            //Make a new array of arguments that doesn't contain the protocol word.
            String args[] = new String[messageSplit.length - 1];
            for (int i = 0; i < messageSplit.length - 1; i++) {
                args[i] = messageSplit[i + 1];
            }

            switch (protocolMessage) {
                case "PUBKEY":
                    PublicKey publicKey = Crypto.decodePublicKey(Crypto.decodeBase64(args[0]));
                    serverConnection.setPublicKey(publicKey);
                    serverConnection.requestToken();
                    shutDown();
                    break;
                //TODO: error handling
            }
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
