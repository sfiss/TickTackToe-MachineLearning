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

        /**
         * Player whose turn it is
         */
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


    private State gameState;

    public TicTacToeGame(GameDTO currentState) {
        gameState = new State(currentState.getGame(), currentState.getToPlay());
    }

    public TicTacToeGame(State currentState) {
        gameState = currentState;
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
                // add a Move that sets the free field to the current player
                .map(p -> new Move(p.getLeft(), p.getMiddle(), state.player))
                .collect(Collectors.toList());
    }

    @Override
    public TicTacToeGame.State getState() {
        return gameState;
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
}
