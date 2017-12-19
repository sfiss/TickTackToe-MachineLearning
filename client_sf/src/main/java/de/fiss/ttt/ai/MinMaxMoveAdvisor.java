package de.fiss.ttt.ai;

import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MinMaxMoveAdvisor<S, M extends Move<S>, B extends Board<S, M>> implements MoveAdvisor<B, M> {

    protected int maxDepth = 6;//Integer.MAX_VALUE;

    protected boolean isLeaf(B board, S state) {
        return board.getPossibleMoves(state).size() == 0;
    }

    protected abstract int evaluate(B board, S s);

    public Pair<Integer, Optional<M>> minimax(B board, S state, int depth, boolean isMaximizing) {

        // base:
        if(depth == 0 || isLeaf(board, state))
            return Pair.of(evaluate(board, state), Optional.empty());

        int bestValue;
        M bestMove = null;

        if(isMaximizing) {
            bestValue = Integer.MIN_VALUE;
            for(M move : board.getPossibleMoves(state))  {
                S child = move.apply(state);
                int result = minimax(board, child, depth-1, !isMaximizing).getLeft();
                if( result > bestValue) {
                    bestValue = result;
                    bestMove = move;
                }
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for(M move : board.getPossibleMoves(state))  {
                S child = move.apply(state);
                int result = minimax(board, child, depth-1, !isMaximizing).getLeft();
                if( result < bestValue) {
                    bestValue = result;
                    bestMove = move;
                }
            }
        }

        return Pair.of(bestValue, Optional.ofNullable(bestMove));
    }

    @Override
    public Optional<M> suggestMove(B board) {
        if(board.getPossibleMoves(board.getState()).size() == 0)
            return Optional.empty();
        S state = board.getState();
        return minimax(board, state, maxDepth, true).getRight();
    }
}
