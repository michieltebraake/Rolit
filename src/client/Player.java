package client;

import util.Mark;

public abstract class Player {
    Mark mark;
    String name;

    public Player(Mark mark, String name) {
        this.mark = mark;
        this.name = name;
    }

    public Mark getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public abstract int determineMove(Board board);

    public void makeMove(Board board) {
        //game.takeTurn(determineMove(game));
        //TODO Send move to server via protocol
    }
}
