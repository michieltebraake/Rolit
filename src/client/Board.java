package client;

import java.util.HashMap;
import java.util.Map;

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
     *
     * @param players number of players in the game
     */
    public Board(int players) {
        this.players = players;
        fields = new Mark[DIM * DIM];
        resetBoard();

    }

    /**
     * Returns FieldID.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return FieldID
     */
    public int getFieldID(int x, int y) {
        return x + DIM * y;
    }

    /**
     * Returns an array with an x an y coordinate.
     *
     * @param fieldID FieldID
     * @return array containing [x,y]
     */
    public int[] getCoordinates(int fieldID) {
        int x = fieldID % DIM;
        int y = (int) Math.floor(fieldID / DIM);
        return new int[]{x, y};
    }

    /**
     * Sets a field to a given mark.
     *
     * @param fieldID fieldID
     * @param mark    mark
     */
    public void setField(int fieldID, Mark mark) {
        fields[fieldID] = mark;
    }

    /**
     * Sets a field to a given mark.
     *
     * @param x    x-coordinate
     * @param y    y-coordinate
     * @param mark mark
     */
    public void setField(int x, int y, Mark mark) {
        fields[getFieldID(x, y)] = mark;
    }

    /**
     * Returns the value of a given field.
     *
     * @param fieldID fieldID.
     * @return mark
     */
    public Mark getField(int fieldID) {
        return fields[fieldID];
    }

    /**
     * Returns the value of a given field.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return mark
     */
    public Mark getField(int x, int y) {
        return fields[getFieldID(x, y)];
    }

    /**
     * Tests if the entire board is full.
     *
     * @return true if all fields are occupied.
     */
    public boolean isFull() {
        for (Mark mark : fields) {
            if (mark == Mark.EMPTY) {
                return false;
            }
        }
        return true;
    }

    public Mark getWinner() {
        HashMap<Mark, Integer> markFields = new HashMap<>();

        for (Mark mark : fields) {
            if(markFields.containsKey(mark)){
                markFields.put(mark, markFields.get(mark) + 1);
            } else {
                markFields.put(mark, 1);
            }
        }

        Map.Entry<Mark,Integer> maxEntry = null;
        for(Map.Entry<Mark,Integer> entry : markFields.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }

    /**
     * Checks if given location is valid (fits on the board)
     *
     * @param x x-coordinate
     * @param y x-coordinate
     * @return valid whether location is valid
     */
    public static boolean validLocation(int x, int y) {
        return x >= 0 && x < Board.DIM && y >= 0 && y < Board.DIM;
    }

    public int getPlayers() {
        return players;
    }


    /**
     * Resets the board to the initial position of a game.
     */
    public void resetBoard() {
        for (int i = 0; i < fields.length; i++) {
            setField(i, Mark.EMPTY);
        }
        setField(3, 4, Mark.BLUE);
        setField(3, 3, Mark.RED);
        setField(4, 4, Mark.GREEN);
        setField(4, 3, Mark.YELLOW);
    }
}
