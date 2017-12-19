package de.fiss.ttt;

import java.util.Arrays;

public class Game {
    public static volatile int[][] game = new int[3][3];

    synchronized static void init() {
        game = new int[3][3];
    }

    synchronized static void play(int row, int col, int player) {
        if(row < 0 || col < 0 ||
                row >= game.length ||
                col >= game[0].length ||
                game[row] [col] != 0 ||
                (player != 1 && player != 2)) {
            throw new IllegalArgumentException("Illegal Move");
        }

        game[row][col] = player;
    }

    synchronized static int getWinner() {
        return 0;
    }

    public static GameDTO getGame() {
         return new GameDTO(game);
    }
}
