package client;

import util.Board;
import util.Mark;
import util.Player;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
    }

    @Override
    public int determineMove(Board board) {
        return 0;
    }
}
