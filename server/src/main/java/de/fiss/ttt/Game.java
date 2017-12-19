package de.fiss.ttt;

import java.util.Arrays;
import java.util.Random;

public class Game {
    private static volatile int[][] game = new int[3][3];
    private static volatile int toPlay = new Random().nextInt() % 2 + 1;

    synchronized static void play(int row, int col, int player) {
        if(row < 0 || col < 0 ||
                row >= game.length ||
                col >= game[0].length ||
                game[row] [col] != 0) {
            throw new IllegalArgumentException("Illegal Move");
        }

        if(player != toPlay)
            throw new IllegalArgumentException("Not players turn");

        game[row][col] = player;

        toPlay = isFinished() ? 0 : 3 - player;
    }

    synchronized static boolean isFinished() {
        return false;
    }

    synchronized static int getWinner() {
        return 0;
    }

    public static GameDTO getGame() {
         return new GameDTO(game, getWinner(), toPlay);
    }
}
