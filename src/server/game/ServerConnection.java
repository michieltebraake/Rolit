package server.game;

import util.Protocol;
import server.ServerPeer;

public class ServerConnection {
    private ServerPeer peer;
    private Game game;

    public ServerConnection(ServerPeer peer) {
        this.peer = peer;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void startGame(ConnectedPlayer[] players) {
        StringBuilder message = new StringBuilder();
        for (ConnectedPlayer player : players) {
            message.append(player.getName());
            message.append(" ");
        }
        peer.send(Protocol.START_GAME + " " + message.toString());
    }

    public void requestMove() {
        peer.send(Protocol.REQUEST_MOVE);
    }

    public void broadcastMove(int field) {
        peer.send(Protocol.BROADCAST_MOVE + " " + field);
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
                case Protocol.MAKE_MOVE:
                    int field = Integer.parseInt(args[0]);
                    game.takeTurn(field);
                    break;
            }
        }
    }
}
