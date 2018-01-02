package de.fiss.ttt.ai.tictactoe;

import de.fiss.ttt.ai.MoveAdvisor;
import de.fiss.ttt.ai.mcts.MCTS;
import de.fiss.ttt.model.mcts.Game;
import de.fiss.ttt.model.tictactoe.TTTGame;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;

import java.util.Optional;

public class TicTacToeMCTS2Adapter implements MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> {

    @Override
    public Optional<TicTacToeGame.Move> suggestMove(TicTacToeGame board) {
        MCTS<TTTGame.TTTState, TTTGame.TTTMove, TTTGame> mcts = new MCTS<>();
        TTTGame.TTTState state = new TTTGame.TTTState(board.getState().game, board.getState().player);
        Optional<TTTGame.TTTMove> move = mcts.suggestMove(new TTTGame(), state, state.player == 1 ? Game.Player.ONE : Game.Player.TWO);
        return move.map(m -> new TicTacToeGame.Move(m.row, m.col, m.value));
    }
}
