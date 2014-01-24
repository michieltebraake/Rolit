package server.game;

import client.GUI.RolitView;

import javax.swing.*;
import java.util.List;
import java.util.Observable;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class Game extends Observable {
    private Board board;
    private RolitView rolitView;

    private int current = 0;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        board = new Board(new ConnectedPlayer[]{new ConnectedPlayer("test1", Mark.RED), new ConnectedPlayer("test2", Mark.YELLOW)}); //TODO Grab players from protocol joins
        //rolitView = new RolitView(this);
        //this.addObserver(rolitView);

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
            makeMove();
        } else if (!rollFields.isEmpty()) {
            board.setField(x, y, currentMark);
            for (int field : rollFields) {
                board.setField(field, currentMark);
            }
            makeMove();
        }
    }

    private void makeMove() {
        current = current++;
        setChanged();
        notifyObservers();

        if (board.isFull()) {
            JOptionPane.showMessageDialog(rolitView, board.getWinner().toString() + " has won the game!", "Game over!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        /*if (players[current].getMark() != Mark.RED && players[current].getMark() != Mark.GREEN) {
            SmartStrategy smartStrategy = new SmartStrategy(this);
            takeTurn(smartStrategy.determineMove(board, players[current].getMark()));
        }*/ //TODO Replace this with sending a request to client for a move.
    }
}
