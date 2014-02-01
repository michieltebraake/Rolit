package server.game;

import util.Board;

public class ServerBoard extends Board {
    private ConnectedPlayer[] players;

    /**
     * Constructs a board.
     *
     * @param players array of players in the game
     */
    public ServerBoard(ConnectedPlayer[] players) {
        super(players);
        this.players = players;
    }

    @Override
    public ConnectedPlayer[] getPlayers() {
        return players;
    }
}
