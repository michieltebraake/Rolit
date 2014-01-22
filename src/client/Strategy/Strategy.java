package client.Strategy;

import client.Board;
import client.Mark;

public interface Strategy {
    /**
     * Get the name of the stragey
     * @return name
     */
    public String getName();

    /**
     * Determine the next AI move
     * @param board
     * @param mark
     * @return
     */
    public int determineMove(Board board, Mark mark);
}