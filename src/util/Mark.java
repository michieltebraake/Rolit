package util;

/**
 * @author Rick Fontein en Michiel te Braake
 * @version 0.1
 */

import java.awt.*;

/**
 * Values represented in a field.
 */
public enum Mark {
    EMPTY(null),
    RED(new Color(255, 68, 68)),
    YELLOW(new Color(255, 187, 51)),
    GREEN(new Color(153, 204, 0)),
    BLUE(new Color(51, 181, 229));

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

    public static Mark[] getMarks(int players) {
        if (players == 2) {
            return new Mark[]{Mark.RED, Mark.GREEN};
        } else if (players == 3) {
            return new Mark[]{Mark.RED, Mark.YELLOW, Mark.GREEN};
        } else {
            return new Mark[]{Mark.RED, Mark.YELLOW, Mark.GREEN, Mark.BLUE};
        }
    }
}


