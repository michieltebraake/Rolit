package client;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */
public class Board {
    public static final int DIM = 8;
    private Mark[] fields;
    private int players;

    /**
     * Constructs a board.
     * @param players number of players in the game
     */
    public Board(int players) {
        this.players = players;
        fields = new Mark[DIM * DIM];
        resetBoard();

    }

    /**
     * Returns FieldID.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return FieldID
     */
    public int getFieldID(int x, int y) {
        return DIM * x + y;
    }

    /**
     * Sets a field to a given mark.
     * @param fieldID fieldID
     * @param mark mark
     */
    public void setField(int fieldID, Mark mark) {
        fields[fieldID] = mark;
    }

    /**
     * Sets a field to a given mark.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param mark mark
     */
    public void setField(int x, int y, Mark mark) {
        fields[getFieldID(x, y)] = mark;
    }

    /**
     * Returns the value of a given field.
     * @param fieldID fieldID.
     * @return mark
     */
    public Mark getField(int fieldID) {
        return fields[fieldID];
    }

    /**
     * Returns the value of a given field.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return mark
     */
    public Mark getField(int x, int y) {
        return fields[getFieldID(x, y)];
    }

    /**
     * Resets the board to the initial position of a game.
     */
    public void resetBoard() {
        for (int i = 0; i < fields.length; i++) {
            setField(i, Mark.EMPTY);
        }
        setField(4, 4, Mark.BLUE);
        setField(4, 5, Mark.RED);
        setField(5, 4, Mark.GREEN);
        setField(5, 5, Mark.YELLOW);
    }
}
