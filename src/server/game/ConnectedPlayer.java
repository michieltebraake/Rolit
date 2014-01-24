package server.game;

public class ConnectedPlayer {
    private String name;
    private Mark mark;

    public ConnectedPlayer(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }
}
