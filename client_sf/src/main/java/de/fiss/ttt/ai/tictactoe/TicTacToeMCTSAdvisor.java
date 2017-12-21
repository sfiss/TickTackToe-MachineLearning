package de.fiss.ttt.ai.tictactoe;

import de.fiss.ttt.ai.AlphaBetaMoveAdvisor;
import de.fiss.ttt.ai.MCTSAdvisor;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;

public class TicTacToeMCTSAdvisor
        extends MCTSAdvisor<TicTacToeGame.State, TicTacToeGame, TicTacToeGame.Move> {

   @Override
    protected double evaluate(TicTacToeGame board, TicTacToeGame.State atState, TicTacToeGame.State resultState) {
       int winner = board.getWinner(resultState.game);
       if(winner == 0)
           return 0;
       // TODO: winner should not be the one whose turn it is?
       return winner != atState.player ? 1 : -1;
    }
}
