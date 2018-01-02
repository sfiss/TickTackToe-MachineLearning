package de.fiss.ttt.model.tictactoe;

import de.fiss.ttt.model.mcts.Game;
import de.fiss.ttt.model.mcts.Move;
import de.fiss.ttt.model.mcts.State;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TTTGame extends Game<TTTGame.TTTState, TTTGame.TTTMove> {
    @Override
    public boolean isFinished(TTTState state) {
        return getPossibleMoves(state).size() == 0;
    }

    @Override
    public Collection<TTTMove> getPossibleMoves(TTTState state) {
        return getPossibleMoves(state.game, state.player);
    }

    @Override
    public Player getWinner(TTTState state) {
        switch (getWinner(state.game)) {
            case 1:
                return Player.ONE;
            case 2:
                return Player.TWO;
            default:
                return Player.NONE;
        }
    }

    public Collection<TTTMove> getPossibleMoves(int[][] game, int player) {
        int dimension = game.length;

        // if somebody already won, no moves
        if(getWinner(game) != 0)
            return Collections.emptyList();

        // all fields are playable
        return IntStream.range(0, dimension)
                .mapToObj(row -> IntStream.range(0, dimension)
                        .mapToObj(col -> Triple.of(row, col, game[row][col])))
                // get all (row, col, value) triples
                .flatMap(x -> x)
                // filter those that are free to play
                .filter(p -> p.getRight() == 0)
                // add a Move that sets the free field to the current player
                .map(p -> new TTTMove(p.getLeft(), p.getMiddle(), player))
                .collect(Collectors.toList());
    }

    private int getWinner(int[] row) {
        int firstValue = row[0];
        return Arrays.stream(row).allMatch(i -> i == firstValue) ? firstValue : 0;
    }

    public int getWinner(int[][] game) {
        int boardSize = game.length;
        int maxIndex = boardSize - 1;
        int[] diag1 = new int[boardSize];
        int[] diag2 = new int[boardSize];

        for (int i = 0; i < boardSize; i++) {
            int[] row = game[i];
            int[] col = new int[boardSize];
            for (int j = 0; j < boardSize; j++) {
                col[j] = game[j][i];
            }

            int checkRowForWin = getWinner(row);
            if(checkRowForWin!=0)
                return checkRowForWin;

            int checkColForWin = getWinner(col);
            if(checkColForWin!=0)
                return checkColForWin;

            diag1[i] = game[i][i];
            diag2[i] = game[maxIndex - i][i];
        }

        int checkDia1gForWin = getWinner(diag1);
        if(checkDia1gForWin!=0)
            return checkDia1gForWin;

        int checkDiag2ForWin = getWinner(diag2);
        if(checkDiag2ForWin!=0)
            return checkDiag2ForWin;

        return 0;
    }

    @RequiredArgsConstructor
    public static class TTTState implements State {
        public final int[][] game;
        public final int player;

        public TTTState set(int row, int col, int value) {
            assert value == player;
            int[][] newState = new int[game.length][game.length];
            for (int i = 0; i < game.length; i++)
                for (int j = 0; j < game.length; j++)
                    newState[i][j] = game[i][j];
            newState[row][col] = value;
            return new TTTState(newState, 3 - player);
        }

        public int get(int row, int col) {
            return game[row][col];
        }
    }

    @RequiredArgsConstructor
    public static class TTTMove implements Move<TTTState> {
        public final int row;
        public final int col;
        public final int value;

        @Override
        public TTTState apply(TTTState state) {
            return state.set(row, col, value);
        }
    }
}
