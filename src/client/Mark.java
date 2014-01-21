package client;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

/**
 * Values represented in a field.
 */
public enum Mark {
    EMPTY, RED, YELLOW, GREEN, BLUE;

    public Mark next(Mark currentMark, int players) {
        switch (currentMark) {
            case RED:
                return Mark.YELLOW;
            case YELLOW:
                return Mark.GREEN;
            case GREEN:
                return Mark.BLUE;
            case BLUE:
                return Mark.RED;
        }
    }
}
