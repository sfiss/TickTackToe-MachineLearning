package de.fiss.ttt;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {
    private static Integer[] scores = { 0, 0 , 0};

    private static final int dimension = 4;
    private static volatile int[][] game = new int[dimension][dimension];
    private static volatile int toPlay = 1; // new Random().nextInt() % 2 + 1

    synchronized static void init() {
        game = new int[dimension][dimension];
        toPlay = new Random().nextInt() % 2 + 1;
        toPlay = toPlay == 0 ? 1 : toPlay; // TODO check this
    }

    synchronized static void play(int row, int col, int player) {
        if(row < 0 || col < 0 ||
                row >= dimension ||
                col >= dimension ||
                game[row] [col] != 0) {
            System.out.println("ERROR: Illegal move! Game: " + Arrays.deepToString(game));
            System.out.println("Player " + player + " played (" + row + "," + col + ")");
            return;
        }

        if(player != toPlay) {
            System.out.println("ERROR: Not player " + player + " turn!");
            return;
        }

        game[row][col] = player;

        toPlay = isFinished() ? 0 : 3 - player;

        // log and reset
        if(isFinished()) {
            scores[getWinner()]++;
            System.out.println("**** Score "
                    + String.join(" - ", Arrays.asList(scores).stream().map(s -> Integer.toString(s)).collect(Collectors.toList()))
                    + " \ngame "
                    + Arrays.deepToString(game));
            Game.init();
        }
    }

    synchronized static boolean isFinished() {
        return getWinner() != 0 || Arrays.stream(game).flatMap(row -> Arrays.stream(row).boxed()).allMatch(field -> field != 0);
    }

    synchronized static int getWinner() {
        // check rows
        outer:
        for (int row = 0; row < dimension; row++) {
            int value = game[row][0];
            if (value == 0)
                continue outer;
            inner:
            for (int col = 1; col < dimension; col++) {
                if (game[row][col] != value)
                    continue outer;
            }
            return value;
        }

        // check columns
        outer:
        for (int row = 0; row < dimension; row++) {
            int value = game[0][row];
            if (value == 0)
                continue outer;
            inner:
            for (int col = 1; col < dimension; col++) {
                if (game[col][row] != value)
                    continue outer;
            }
            return value;
        }

        // check diagonal 1
        int winner = game[0][0];
        for (int i = 1; i < dimension; i++) {
            if (game[i][i] != winner) {
                winner = 0;
                break;
            }
        }
        if(winner > 0)
            return winner;

        winner = game[dimension-1][dimension-1];
        for (int i = dimension-1; i >= 0; i--) {
            if (game[i][i] != winner) {
                winner = 0;
                break;
            }
        }

        if(winner > 0)
            return winner;

        return 0;
    }



    public static GameDTO getGame() {
         return new GameDTO(game, getWinner(), toPlay);
    }
}
