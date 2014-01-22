package client;

import client.GUI.RolitView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class Game extends Observable {
    private Board board;
    private RolitView rolitView;

    private Mark current = Mark.RED;

    private final static int[] xMoves = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
    private final static int[] yMoves = new int[]{1, 1, 0, -1, -1, -1, 0, 1};

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        board = new Board(4);
        rolitView = new RolitView(this);
        this.addObserver(rolitView);

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
    public Mark getCurrent() {
        return current;
    }

    /**
     * Returns all the ids of the fields that would be changed to the current mark with the given move.
     *
     * @param x x-coordinate of the move that is being made
     * @param y y-coordinate of the move that is being made
     * @return rollFields list of fields that are changed by this move
     */
    public List<Integer> getRollFields(int x, int y) {
        List<Integer> rollFields = new ArrayList<>();
        for (int i = 0; i < Board.DIM; i++) {
            List<Integer> fieldsToAdd = getRollFields(x, y, xMoves[i], yMoves[i]);
            if (fieldsToAdd != null) {
                rollFields.addAll(fieldsToAdd);
            }
        }
        return rollFields;
    }

    private List<Integer> getRollFields(int x, int y, int addX, int addY) {
        List<Integer> otherMarkFields = new ArrayList<>();
        boolean foundOwnMark = false;

        //Add to x & y before starting to loop (don't want to check the field that is being changed)
        x += addX;
        y += addY;

        while (x >= 0 && x < Board.DIM && y >= 0 && y < Board.DIM) {
            if (board.getField(x, y) == Mark.EMPTY) {
                break;
            } else if (board.getField(x, y) != current) {
                otherMarkFields.add(board.getFieldID(x, y));
            } else {
                foundOwnMark = true;
                break;
            }

            x += addX;
            y += addY;
        }
        return foundOwnMark ? otherMarkFields : null;
    }

    private boolean canFormLineFrom(int x, int y) {
        for (int i = 0; i < Board.DIM; i++) {
            if (canFormLineFrom(x, y, xMoves[i], yMoves[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean canFormLineFrom(int x, int y, int addX, int addY) {
        boolean foundOtherMark = false;

        //Add to x & y before starting to loop (don't want to check the field that is being changed)
        x += addX;
        y += addY;

        while (x >= 0 && x < Board.DIM && y >= 0 && y < Board.DIM) {
            if (board.getField(x, y) == Mark.EMPTY) {
                return foundOtherMark;
            } else if (board.getField(x, y) != current) {
                foundOtherMark = true;
            } else {
                return false;
            }

            x += addX;
            y += addY;
        }

        return false;
    }

    private boolean hasMark(Mark mark) {
        for (int i = 0; i < 64; i++) {
            if (board.getField(i) == mark) {
                return true;
            }
        }
        return false;
    }

    private boolean canMakeMove() {
        for (int i = 0; i < Board.DIM * Board.DIM; i++) {
            if (board.getField(i) == current) {
                if (canFormLineFrom(board.getCoordinates(i)[0], board.getCoordinates(i)[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean nextToBall(int x, int y) {
        for (int i = 0; i < Board.DIM; i++) {
            if (Board.validLocation(x + xMoves[i], y + yMoves[i]) && board.getField(x + xMoves[i], y + yMoves[i]) != Mark.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resets the board to the initial state.
     */
    public void reset() {
        current = Mark.RED;
        board.resetBoard();
        setChanged();
        notifyObservers();
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
        List<Integer> rollFields = getRollFields(x, y);

        if(!canMakeMove() && nextToBall(x, y)){
            board.setField(x, y, current);
            makeMove();
        } else if (!rollFields.isEmpty()) {
            board.setField(x, y, current);
            for (int field : rollFields) {
                board.setField(field, current);
            }
            makeMove();
        }
    }

    private void makeMove() {
        current = current.next(current, board.getPlayers());
        setChanged();
        notifyObservers();

        if (board.isFull()) {
            JOptionPane.showMessageDialog(rolitView, board.getWinner().toString() + " has won the game!", "Game over!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
