package client;

public class HumanPlayer extends Player {
    public HumanPlayer(Mark mark, String name) {
        super(mark, name);
    }

    @Override
    public int determineMove(Board board) {
        return 0;
    }
}
