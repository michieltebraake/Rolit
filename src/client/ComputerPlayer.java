package client;

import client.Strategy.Strategy;
import util.Board;
import util.Mark;
import util.Player;

public class ComputerPlayer extends Player {
    private Strategy strategy;

    public ComputerPlayer(String name, Mark mark, Strategy strategy) {
        super(name, mark);
        this.strategy = strategy;
    }

    @Override
    public int determineMove(Board board) {
        return strategy.determineMove(board, mark);
    }
}
