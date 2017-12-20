package de.fiss.ttt.ai.tictactoe;

import de.fiss.ttt.ai.AlphaBetaMoveAdvisor;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;

public class TicTacToeMinMaxAdvisor
        //extends MinMaxMoveAdvisor<TicTacToeGame.State, TicTacToeGame.Move, TicTacToeGame> {
        extends AlphaBetaMoveAdvisor<TicTacToeGame.State, TicTacToeGame, TicTacToeGame.Move> {

    @Override
    protected int evaluate(TicTacToeGame board, TicTacToeGame.State state) {
        return board.getWinner(state.game) == 0 ? 0 :
                board.getWinner(state.game) == board.getState().player ? 1 : -1;
    }
}
