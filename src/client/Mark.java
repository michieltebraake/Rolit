package client;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

import java.awt.*;

/**
 * Values represented in a field.
 */
public enum Mark {
    EMPTY(Color.WHITE),
    RED (Color.RED),
    YELLOW (Color.YELLOW),
    GREEN (Color.GREEN),
    BLUE (Color.BLUE);

    private Color color;

    private Mark(Color color){
        this.color = color;
    }

    /**
     * @return color the board color for the mark
     */
    public Color getColor(){
        return color;
    }

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
            default:
                return null;
        }
    }
}
