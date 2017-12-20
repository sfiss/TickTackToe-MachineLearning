package de.fiss.ttt.ai.tictactoe;

import de.fiss.ttt.ai.MoveAdvisor;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TicTacToeSmartMoveAdvisor implements MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> {



    @Override
    public Optional<TicTacToeGame.Move> suggestMove(TicTacToeGame board) {
        List<TicTacToeGame.Move> moves = new ArrayList<>(board.getPossibleMoves(board.getState()));

        if(board.getState().getDimension() != 3)
            return moves.stream().findAny();

        // preferences: middle
        Collections.sort(moves, (m1, m2) -> {
            Function<TicTacToeGame.Move, Boolean> isMiddle = m -> m.row == 1 && m.col == 1;
            return isMiddle.apply(m1) ? 1 : isMiddle.apply(m2) ? -1 : 0;
        });

        return Optional.of(moves.get(1));
    }
}
