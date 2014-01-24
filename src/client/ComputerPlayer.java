package client;

import client.Strategy.Strategy;

public class ComputerPlayer extends Player {
    private Strategy strategy;

    public ComputerPlayer(Mark mark, String name, Strategy strategy) {
        super(mark, name);
        this.strategy = strategy;
    }

    @Override
    public int determineMove(Board board) {
        return strategy.determineMove(board, mark);
    }
}
