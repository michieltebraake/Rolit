package client.Strategy;

import client.Board;
import client.Game;
import client.Mark;

public class SmartStrategy implements Strategy {
    private Game game;

    public SmartStrategy(Game game){
        this.game = game;
    }

    @Override
    public String getName() {
        return "Smart Strategy";
    }

    @Override
    public int determineMove(Board board, Mark mark) {
        if(game.canMakeMove()){
            int bestField = -1;
            int mostFlips = -1;
            for (int i = 0; i < Board.DIM * Board.DIM; i++) {
                if (board.getField(i) == Mark.EMPTY) {
                    int[] coordinates = board.getCoordinates(i);
                    int flips = game.getRollFields(coordinates[0], coordinates[1]).size();
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
                    if(game.nextToBall(coordinates[0], coordinates[1])){
                        return i;
                    }
                }
            }
        }

        return 0;
    }
}
