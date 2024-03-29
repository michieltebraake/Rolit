package util;

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

    //@ requires players != null && players.length != 0;
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

    //@ ensures \result != null && \result.length != 0;
    /**
     * @return Player[] array of players in the game
     */
    public /*@ pure @*/ Player[] getPlayers() {
        return players;
    }

    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    /**
     * Returns FieldID.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return FieldID
     */
    public /*@ pure @*/ int getFieldID(int x, int y) {
        return x + DIM * y;
    }

    //@ requires fieldID >= 0 && fieldID <= Board.DIM * Board.DIM;
    /**
     * Returns an array with an x an y coordinate.
     *
     * @param fieldID FieldID
     * @return array containing [x,y]
     */
    public /*@ pure @*/ int[] getCoordinates(int fieldID) {
        int x = fieldID % DIM;
        int y = (int) Math.floor(fieldID / DIM);
        return new int[]{x, y};
    }

    //@ requires fieldID >= 0 && fieldID <= Board.DIM * Board.DIM;
    //@ requires mark != null;
    //@ ensures getField(fieldID) == mark;
    /**
     * Sets a field to a given mark.
     *
     * @param fieldID fieldID
     * @param mark    mark
     */
    public void setField(int fieldID, Mark mark) {
        fields[fieldID] = mark;
    }

    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    //@ requires mark != null;
    //@ ensures getField(x, y) == mark;
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

    //@ requires fieldID >= 0 && fieldID <= Board.DIM * Board.DIM;
    /**
     * Returns the value of a given field.
     *
     * @param fieldID fieldID.
     * @return mark
     */
    public /*@ pure @*/ Mark getField(int fieldID) {
        return fields[fieldID];
    }

    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    /**
     * Returns the value of a given field.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return mark
     */
    public /*@ pure @*/ Mark getField(int x, int y) {
        return fields[getFieldID(x, y)];
    }

    /**
     * Tests if the entire board is full.
     *
     * @return true if all fields are occupied.
     */
    public /*@ pure @*/ boolean isFull() {
        for (Mark mark : fields) {
            if (mark == Mark.EMPTY) {
                return false;
            }
        }
        return true;
    }

    /**
     * Loops through all the fields on the board to find the player with the most marks on the board.
     *
     * @return winner Mark of player with the most marks on the board
     */
    public /*@ pure @*/ Mark getWinner() {
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

    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    /**
     * Checks if given location is valid (fits on the board)
     *
     * @param x x-coordinate
     * @param y x-coordinate
     * @return valid whether location is valid
     */
    public /*@ pure @*/ static boolean validLocation(int x, int y) {
        return x >= 0 && x < Board.DIM && y >= 0 && y < Board.DIM;
    }

    //@ requires mark != null;
    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    //@ ensures \result != null;
    /**
     * Returns all the ids of the fields that would be changed to the current mark with the given move.
     *
     * @param x x-coordinate of the move that is being made
     * @param y y-coordinate of the move that is being made
     * @return rollFields list of fields that are changed by this move
     */
    public /*@ pure @*/ List<Integer> getRollFields(Mark mark, int x, int y) {
        List<Integer> rollFields = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<Integer> fieldsToAdd = getRollFields(mark, x, y, xMoves[i], yMoves[i]);
            if (fieldsToAdd != null) {
                rollFields.addAll(fieldsToAdd);
            }
        }
        return rollFields;
    }

    private /*@ pure @*/ List<Integer> getRollFields(Mark mark, int x, int y, int addX, int addY) {
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

    //@ requires mark != null;
    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    /**
     * Tests if a line can be formed with given mark from given x and y.
     * The given x and y coordinates should be empty.
     * Returns true when an opponents mark is found and after that the players mark is found (without an empty field in between).
     *
     * @param mark Mark to form a line with
     * @param x    x-coordinate to check from
     * @param y    y-coordinate to check from
     * @return whether or not a line can be made
     */
    public /*@ pure @*/ boolean canFormLineToOwnMark(Mark mark, int x, int y) {
        for (int i = 0; i < 8; i++) {
            if (canFormLineToOwnMark(mark, x, y, xMoves[i], yMoves[i])) {
                return true;
            }
        }
        return false;
    }

    private /*@ pure @*/ boolean canFormLineToOwnMark(Mark mark, int x, int y, int addX, int addY) {
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

    //@ requires mark != null;
    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    /**
     * Tests if given mark can form a line to given x and y coordinates.
     * The given x and y coordinates should have the given mark there.
     * Returns true when an opponents mark is found and after that an empty field is found (without the players mark in between).
     *
     * @param mark Mark to form a line with
     * @param x    x-coordinate to check from
     * @param y    y-coordinate to check from
     * @return whether or not a line can be made
     */
    public /*@ pure @*/ boolean canFormLineFromOwnMark(Mark mark, int x, int y) {
        for (int i = 0; i < 8; i++) {
            if (canFormLineFromOwnMark(mark, x, y, xMoves[i], yMoves[i])) {
                return true;
            }
        }
        return false;
    }

    private /*@ pure @*/ boolean canFormLineFromOwnMark(Mark mark, int x, int y, int addX, int addY) {
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

    //@ requires mark != null;
    /**
     * Checks if given mark can make a move (form a line).
     *
     * @param mark Mark to check
     * @return true if player can form a line
     */
    public /*@ pure @*/ boolean canMakeMove(Mark mark) {
        for (int i = 0; i < DIM * DIM; i++) {
            if (getField(i) == mark) {
                if (canFormLineFromOwnMark(mark, getCoordinates(i)[0], getCoordinates(i)[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    //@ requires x >= 0 && x < Board.DIM;
    //@ requires y >= 0 && y < Board.DIM;
    /**
     * Checks if there is a ball next to given x and y coordinates.
     *
     * @param x x-coordinate to check around
     * @param y y-coordinate to check around
     * @return true if there is a ball next to the given location
     */
    public /*@ pure @*/ boolean nextToBall(int x, int y) {
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
        setField(DIM / 2, DIM / 2, Mark.GREEN);
    }
}
