package server.game;

import java.util.List;
import java.util.Observable;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class Game extends Observable {
    private Board board;

    private int current = 0;

    public Game(ConnectedPlayer[] players) {
        board = new Board(players); //TODO Grab players from protocol joins

        //Start the game
        for (ConnectedPlayer player : players) {
            player.getPeer().getServerConnection().startGame(players);
        }
        players[current].getPeer().getServerConnection().requestMove();

        setChanged();
        notifyObservers();
    }

    /**
     * @return board the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return mark the current mark
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Resets the board to the initial state.
     */
    public void reset() {
        current = 0;
        board.resetBoard();
        setChanged();
        notifyObservers();
    }

    /**
     * Places a move at given field id.
     * Will not change anything if move is not valid.
     *
     * @param field field id
     */
    public void takeTurn (int field) {
        int[] coordinates = board.getCoordinates(field);
        takeTurn(coordinates[0], coordinates[1]);
    }

    /**
     * Places a move at given x and y coordinate.
     * Will not change anything if move is not valid.
     *
     * @param x x-coordinate of move
     * @param y y-coordinate of move
     */
    public void takeTurn(int x, int y) {
        //Check if player has marks on the board & player can make a valid move.
        Mark currentMark = board.getPlayers()[current].getMark();
        List<Integer> rollFields = board.getRollFields(currentMark, x, y);

        if(!board.canMakeMove(currentMark) && board.nextToBall(x, y)){
            board.setField(x, y, currentMark);
            makeMove(board.getFieldID(x, y));
        } else if (!rollFields.isEmpty()) {
            board.setField(x, y, currentMark);
            for (int field : rollFields) {
                board.setField(field, currentMark);
            }
            makeMove(board.getFieldID(x, y));
        }
    }

    private void makeMove(int field) {
        ConnectedPlayer[] players = board.getPlayers();

        System.out.println(current);

        current++;
        current = current % players.length;

        System.out.println(current);

        //Send move to all clients.
        for (ConnectedPlayer player : players) {
            player.getPeer().getServerConnection().broadcastMove(field);
        }

        if (board.isFull()) {
            //Send game over to all clients.
            for (ConnectedPlayer player : players) {
                player.getPeer().getServerConnection().gameOver();
            }
        } else {
            //Request move from next client.
            players[current].getPeer().getServerConnection().requestMove();
        }
    }
}
