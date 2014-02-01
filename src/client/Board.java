package client;

import util.Mark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */
public class Board {
    public static final int DIM = 8;
    private Mark[] fields;
    private Player[] players;

    public final static int[] xMoves = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
    public final static int[] yMoves = new int[]{1, 1, 0, -1, -1, -1, 0, 1};

    /**
     * Constructs a board.
     *
     * @param players array of players in the game
    */
    public Board(Player[] players) {
        this.players = players;
        fields = new Mark[DIM * DIM];
        resetBoard();
    }

    /**
     * @return Player[] array of players in the game
     */
    public Player[] getPlayers() {
        return players;
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
            if (markFields.containsKey(mark)) {
                markFields.put(mark, markFields.get(mark) + 1);
            } else {
                markFields.put(mark, 1);
            }
        }

        Map.Entry<Mark, Integer> maxEntry = null;
        for (Map.Entry<Mark, Integer> entry : markFields.entrySet()) {
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

    /**
     * Returns all the ids of the fields that would be changed to the current mark with the given move.
     *
     * @param x x-coordinate of the move that is being made
     * @param y y-coordinate of the move that is being made
     * @return rollFields list of fields that are changed by this move
     */
    public List<Integer> getRollFields(Mark mark, int x, int y) {
        List<Integer> rollFields = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<Integer> fieldsToAdd = getRollFields(mark, x, y, xMoves[i], yMoves[i]);
            if (fieldsToAdd != null) {
                rollFields.addAll(fieldsToAdd);
            }
        }
        return rollFields;
    }

    private List<Integer> getRollFields(Mark mark, int x, int y, int addX, int addY) {
        List<Integer> otherMarkFields = new ArrayList<>();
        boolean foundOwnMark = false;

        //Add to x & y before starting to loop (don't want to check the field that is being changed)
        x += addX;
        y += addY;

        while (x >= 0 && x < DIM && y >= 0 && y < DIM) {
            if (getField(x, y) == Mark.EMPTY) {
                break;
            } else if (getField(x, y) != mark) {
                otherMarkFields.add(getFieldID(x, y));
            } else {
                foundOwnMark = true;
                break;
            }

            x += addX;
            y += addY;
        }
        return foundOwnMark ? otherMarkFields : null;
    }

    public boolean canFormLineToOwnMark(Mark mark, int x, int y) {
        for (int i = 0; i < 8; i++) {
            if (canFormLineToOwnMark(mark, x, y, xMoves[i], yMoves[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean canFormLineToOwnMark(Mark mark, int x, int y, int addX, int addY) {
        boolean foundOtherMark = false;

        //Add to x & y before starting to loop (don't want to check the field that is being changed)
        x += addX;
        y += addY;

        while (x >= 0 && x < DIM && y >= 0 && y < DIM) {
            if (getField(x, y) == mark) {
                return foundOtherMark;
            } else if (getField(x, y) != mark && getField(x, y) != Mark.EMPTY) {
                foundOtherMark = true;
            } else {
                return false;
            }

            x += addX;
            y += addY;
        }

        return false;
    }

    public boolean canFormLineFromOwnMark(Mark mark, int x, int y) {
        for (int i = 0; i < 8; i++) {
            if (canFormLineFromOwnMark(mark, x, y, xMoves[i], yMoves[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean canFormLineFromOwnMark(Mark mark, int x, int y, int addX, int addY) {
        boolean foundOtherMark = false;

        //Add to x & y before starting to loop (don't want to check the field that is being changed)
        x += addX;
        y += addY;

        while (x >= 0 && x < DIM && y >= 0 && y < DIM) {
            if (getField(x, y) == Mark.EMPTY) {
                return foundOtherMark;
            } else if (getField(x, y) != mark) {
                foundOtherMark = true;
            } else {
                return false;
            }

            x += addX;
            y += addY;
        }

        return false;
    }

    public boolean canMakeMove(Mark mark) {
        for (int i = 0; i < DIM * DIM; i++) {
            if (getField(i) == mark) {
                if (canFormLineFromOwnMark(mark, getCoordinates(i)[0], getCoordinates(i)[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean nextToBall(int x, int y) {
        for (int i = 0; i < 8; i++) {
            if (validLocation(x + xMoves[i], y + yMoves[i]) && getField(x + xMoves[i], y + yMoves[i]) != Mark.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resets the board to the initial position of a game.
     */
    public void resetBoard() {
        for (int i = 0; i < fields.length; i++) {
            setField(i, Mark.EMPTY);
        }
        setField((DIM / 2) - 1, (DIM / 2) - 1, Mark.RED);
        setField((DIM / 2) - 1, (DIM / 2), Mark.BLUE);
        setField((DIM / 2), (DIM / 2) - 1, Mark.YELLOW);
        setField(DIM / 2, DIM  / 2, Mark.GREEN);
    }
}
