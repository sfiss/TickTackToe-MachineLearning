package de.fiss.ttt.ai.random;

import de.fiss.ttt.ai.MoveAdvisor;
import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;

import java.util.*;

public class RandomMoveAdvisor<B extends Board, M extends Move> implements MoveAdvisor<B, M> {

    @Override
    public Optional<M> suggestMove(Board board) {
        List<M> moves = new ArrayList<>(board.getPossibleMoves());
        Collections.shuffle(moves);
        return moves.size() == 0 ? Optional.empty() : Optional.of(moves.get(0));
        //return board.getPossibleMoves().stream().findAny();
    }
}
