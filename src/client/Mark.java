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
    RED(Color.RED),
    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE);

    private Color color;

    private Mark(Color color) {
        this.color = color;
    }

    /**
     * @return color the board color for the mark
     */
    public Color getColor() {
        return color;
    }


    public Mark next(Mark currentMark, int players) {
        switch (players) {
            case 2:
                switch (currentMark) {
                    case RED:
                        return YELLOW;
                    case YELLOW:
                        return RED;
                    default:
                        return null;
                }
            case 3:
                switch(currentMark){
                    case RED:
                        return YELLOW;
                    case YELLOW:
                        return GREEN;
                    case GREEN:
                        return RED;
                    default:
                        return null;
                }
            case 4:
                switch(currentMark){
                    case RED:
                        return YELLOW;
                    case YELLOW:
                        return GREEN;
                    case GREEN:
                        return BLUE;
                    case BLUE:
                        return RED;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
}


