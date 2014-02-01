package test;

import server.RolitServer;
import server.connection.ServerConnection;
import server.game.ConnectedPlayer;
import server.game.Game;
import util.Mark;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Test {
    private Game game;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            new Test().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void start() throws Exception {
        setUp();

        testTakeTurn();

        tearDown();
    }

    void setUp() throws Exception {
        //Create server socket for clients to connect to.
        serverSocket = new ServerSocket(2727);

        //Create 2 players to test with.
        ConnectedPlayer player1 = new ConnectedPlayer("test1", Mark.RED, new ServerConnection(new Socket("localhost", 2727), new RolitServer(), "test1"));
        ConnectedPlayer player2 = new ConnectedPlayer("test2", Mark.GREEN, new ServerConnection(new Socket("localhost", 2727), new RolitServer(), "test2"));
        ConnectedPlayer[] players = {player1, player2};

        //Start a game
        game = new Game(players);
    }

    void tearDown() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void testTakeTurn() throws Exception {
        //Make a move, current should increase by one.
        game.takeTurn(45);
        assertEquals(1, game.getCurrent());

        //Player 2 doesn't have a mark on the field.
        //Attempt to place mark in invalid place (not next to a mark), current should stay the same.
        game.takeTurn(30);
        assertEquals(1, game.getCurrent());

        //Now place mark in valid place.
        game.takeTurn(29);
        assertEquals(0, game.getCurrent());
        
        //Make another invalid move (player can't form line to the selected field), current should stay the same.
        game.takeTurn(31);
        assertEquals(0, game.getCurrent());

        //Make another invalid move (player can't form line to the selected field), current should stay the same.
        game.takeTurn(26);
        assertEquals(0, game.getCurrent());
    }

    public static void assertEquals(Object expected, Object actual) throws Exception {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new Exception("Assert equals failed! Expected: " + expected + ". Actual: " + actual);
    }
}
