package client.Strategy;

import client.Board;
import util.Mark;

public class SmartStrategy implements Strategy {
    private final static int[] cornerPointsX = {0, 7, 0, 7};
    private final static int[] cornerPointsY = {0, 0, 7, 7};

    public SmartStrategy(){
    }

    @Override
    public String getName() {
        return "Smart Strategy";
    }

    @Override
    public int determineMove(Board board, Mark mark) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(board.canMakeMove(mark)){
            int bestField = -1;
            int mostFlips = -1;

            for(int i = 0; i < cornerPointsX.length; i++) {
                if (board.getField(cornerPointsX[i], cornerPointsY[i]) == mark) {
                    int bestLocation = canFormLineFrom(board, mark, cornerPointsX[i], cornerPointsY[i]);
                    if (bestLocation != -1) {
                        return bestLocation;
                    }
                }
            }

            for (int i = 0; i < Board.DIM * Board.DIM; i++) {
                if (board.getField(i) == Mark.EMPTY) {
                    int[] coordinates = board.getCoordinates(i);
                    int flips = board.getRollFields(mark, coordinates[0], coordinates[1]).size();
                    if (flips > mostFlips) {
                        bestField = i;
                        mostFlips = flips;
                    }
                }
            }
            return bestField;
        } else {
            for (int i = 0; i < Board.DIM * Board.DIM; i++) {
                if (board.getField(i) == Mark.EMPTY) {
                    int[] coordinates = board.getCoordinates(i);
                    if(board.nextToBall(coordinates[0], coordinates[1])){
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    public int canFormLineFrom(Board board, Mark mark, int x, int y) {
        int bestLocation = -1;

        for (int i = 0; i < 8; i++) {
            int location = canFormLineFrom(board, mark, x, y, Board.xMoves[i], Board.yMoves[i]);
            if (location > bestLocation) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }

    private int canFormLineFrom(Board board, Mark mark, int x, int y, int addX, int addY) {
        boolean foundOtherMark = false;

        //Add to x & y before starting to loop (don't want to check the field that is being changed)
        x += addX;
        y += addY;

        while (x >= 0 && x < Board.DIM && y >= 0 && y < Board.DIM) {
            if (board.getField(x, y) == Mark.EMPTY) {
                if (foundOtherMark) {
                    return board.getFieldID(x, y);
                } else {
                    return -1;
                }
            } else if (board.getField(x, y) != mark) {
                foundOtherMark = true;
            } else {
                return -1;
            }

            x += addX;
            y += addY;
        }
        return -1;
    }
}
