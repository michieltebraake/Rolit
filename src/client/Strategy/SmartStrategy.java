package client.Strategy;

import client.Board;
import util.Mark;

public class SmartStrategy implements Strategy {
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
}
