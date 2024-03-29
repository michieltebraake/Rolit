package server.game;

import util.Mark;

import java.util.List;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class Game {
    private ServerBoard board;

    private int current = 0;

    //@ requires players != null && !players.isEmpty();
    /**
     * Constructs a game.
     *
     * @param players array of connected players
     */
    public Game(ConnectedPlayer[] players) {
        board = new ServerBoard(players);

        //Start the game
        for (ConnectedPlayer player : players) {
            player.getServerConnection().startGame(players);
        }
        players[current].getServerConnection().requestMove();
    }

    //@ ensures \result != null;
    /**
     * @return board the game board
     */
    public ServerBoard getBoard() {
        return board;
    }

    //@ ensures \result != null;
    /**
     * @return mark the current mark
     */
    public int getCurrent() {
        return current;
    }

    //@ ensures getResult() == 0;
    /**
     * Resets the board to the initial state.
     */
    public void reset() {
        current = 0;
        board.resetBoard();
    }

    //@ requires field >= 0 && field < Board.DIM * Board.DIM;
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

    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
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

        current++;
        current = current % players.length;

        //Send move to all clients.
        for (ConnectedPlayer player : players) {
            player.getServerConnection().broadcastMove(field);
        }

        if (board.isFull()) {
            //Send game over to all clients.
            for (ConnectedPlayer player : players) {
                player.getServerConnection().gameOver();
            }
        } else {
            //Request move from next client.
            players[current].getServerConnection().requestMove();
        }
    }
}
