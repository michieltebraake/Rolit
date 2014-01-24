
package temp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server {
	private static final String USAGE = "usage: java week4.cmdline.Server <name> <port>";

	/** Start een Server-applicatie op. */
	public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(USAGE);
            System.exit(0);
        }

        String name = args[0];
        InetAddress addr = null;
        int port = 0;
        ServerSocket server = null;

        // parse args[1] - the port
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + args[1] + " is not an integer");
            System.exit(0);
        }

        // try to open a Socket to the server
        try {
            server = new ServerSocket(2010);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a  server socket at port " + port);
        }

        // create Peer object and start the two-way communication
        try {
            while(true) {
                Socket clientSocket = server.accept();
                System.out.println("Client connected!");

                Peer client = new Peer(name, clientSocket);
                Thread streamInputHandler = new Thread(client);
                streamInputHandler.start();
                client.handleTerminalInput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // end of class Server
