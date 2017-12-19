package de.fiss.ttt.ai;

import de.fiss.ttt.model.TicTacToeGame;

public class TicTacToeMinMaxAdvisor extends MinMaxMoveAdvisor<TicTacToeGame.State, TicTacToeGame.Move, TicTacToeGame> {

    @Override
    protected int evaluate(TicTacToeGame board, TicTacToeGame.State state) {
        return board.getWinner(state.game) == 0 ? 0 :
                board.getWinner(state.game) == board.getState().player ? 1 : -1;
    }
}
