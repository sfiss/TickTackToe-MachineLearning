package de.fiss.ttt.model.tictactoe;

import de.fiss.ttt.GameDTO;
import de.fiss.ttt.model.Board;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicTacToeGame implements Board<TicTacToeGame.State, TicTacToeGame.Move> {

    public static class Move implements de.fiss.ttt.model.Move<State> {

        public final int row;
        public final int col;
        public final int value;

        public Move(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        @Override
        public State apply(State state) {
            return state.set(row, col, value);
        }

        // TODO hashcode
        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Move))
                return false;
            Move that = (Move) obj;
            return that.col == this.col && that.row == this.row && that.value == this.value;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ", " + value + ")";
        }
    }

    public static class State {
        public final int[][] game;

        public final int player;

        public State(int[][] game, int player) {
            this.game = game;
            this.player = player;
        }

        public State set(int row, int col, int value) {
            assert value == player;
            int[][] newState = new int[game.length][game.length];
            for(int i = 0; i < game.length; i++)
                for(int j = 0; j < game.length; j++)
                    newState[i][j] = game[i][j];
            newState[row][col] = value;
            return new State(newState, 3 - player);
        }

        public int getDimension() {
            return game.length;
        }

        public int get(int row, int col) {
            return game[row][col];
        }

        @Override
        public String toString() {
            return Arrays.deepToString(game) + "(" + player + ")";
        }
    }


    private State state;

    public TicTacToeGame(GameDTO currentState) {
        state = new State(currentState.getGame(), currentState.getToPlay());
    }

    @Override
    public Collection<Move> getPossibleMoves(final State state) {
        int dimension = state.getDimension();

        // if somebody already won, no moves
        if(getWinner(state.game) != 0)
            return Collections.emptyList();

        // all fields are playable
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
    public TicTacToeGame.State getState() {
        return state;
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
}
