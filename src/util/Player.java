package util;

public abstract class Player {
    protected String name;
    protected Mark mark;

    public Player(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    public Mark getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public abstract int determineMove(Board board);
}
