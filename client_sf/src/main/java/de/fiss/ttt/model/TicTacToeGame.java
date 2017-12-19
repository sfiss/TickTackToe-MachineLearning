package de.fiss.ttt.model;

import de.fiss.ttt.GameDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicTacToeGame implements Board<TicTacToeGame.State, TicTacToeGame.Move> {

    public static class Move implements de.fiss.ttt.model.Move<State> {

        public final int row;
        public final int col;
        public final int value;

        Move(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        @Override
        public State apply(State state) {
            return state.set(row, col, value);
        }
    }

    public static class State {
        final int[][] game;

        final int player;

        State(int[][] game, int player) {
            this.game = game;
            this.player = player;
        }

        State set(int row, int col, int value) {
            int[][] newState = game.clone();
            newState[row][col] = value;
            return new State(newState, player);
        }

        int getDimension() {
            return game.length;
        }

        int get(int row, int col) {
            return game[row][col];
        }
    }


    private State state;

    public TicTacToeGame(GameDTO currentState) {
        state = new State(currentState.getGame(), currentState.getToPlay());
    }

    @Override
    public Collection<Move> getPossibleMoves() {
        int dimension = state.getDimension();
        return IntStream.range(0, dimension)
                .mapToObj(row -> IntStream.range(0, dimension)
                        .mapToObj(col -> Triple.of(row, col, state.get(row, col))))
                // get all (row, col, value) triples
                .flatMap(x -> x)
                // filter those that are free to play
                .filter(p -> p.getRight() == 0)
                // add a move that sets the free field to the current player
                .map(p -> new Move(p.getLeft(), p.getMiddle(), state.player))
                .collect(Collectors.toList());
    }

    @Override
    public State getState() {
        return state;
    }
}
