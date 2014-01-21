package client;

import client.GUI.RolitView;

import java.util.Observable;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

public class Game extends Observable {
    private Board board;
    private Mark current;

    public Game(Board board) {
        this.board = board;
        this.addObserver(new RolitView());
    }

    /**
     * @return board the game board
     */
    public Board getBoard(){
        return board;
    }

    /**
     * @return mark the current mark
     */
    public Mark getCurrent(){
        return current;
    }

    public void reset(){
        current = Mark.RED; //TODO check first mark
        board.reset();
        setChanged();
        notifyObservers();
    }


}
