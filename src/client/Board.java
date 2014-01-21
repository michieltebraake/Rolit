package client;

/**
 * Created with IntelliJ IDEA.
 * User: Rick
 * Date: 20-1-14
 * Time: 16:36
 */
public class Board {
    private static final int DIM = 8;
    private Mark[] fields;
    private int players;


    public Board(int players) {
        this.players = players;
        fields = new Mark[DIM * DIM];
        resetBoard();

    }

    public int getFieldID(int x, int y) {
        return DIM * x + y;
    }

    public void setField(int fieldID, Mark mark) {
        fields[fieldID] = mark;
    }

    public void setField(int x, int y, Mark mark) {
        fields[getFieldID(x,y)] = mark;
    }

    public Mark getField(int fieldID) {
        return fields[fieldID];
    }

    public Mark getField(int x, int y) {
        return fields[getFieldID(x, y)];
    }

    public void resetBoard() {
        for (int i = 0; i < fields.length; i++) {
            setField(i, Mark.EMPTY);
        }

    }
}
