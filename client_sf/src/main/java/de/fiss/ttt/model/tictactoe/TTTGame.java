package de.fiss.ttt.model.tictactoe;

import de.fiss.ttt.model.mcts.Game;
import de.fiss.ttt.model.mcts.Move;
import de.fiss.ttt.model.mcts.State;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;

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

    public int getWinner(int[][] game) {
        int dimension = game.length;

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
