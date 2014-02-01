package client.Strategy;

import util.Board;
import util.Mark;

public interface Strategy {
    /**
     * Get the name of the stragey
     * @return name
     */
    public String getName();

    /**
     * Determine the next AI move
     * @param board board to determine move for
     * @param mark mark to be used
     * @return field number
     */
    public int determineMove(Board board, Mark mark);
}