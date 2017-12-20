package de.fiss.ttt.ai;

import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public abstract class AlphaBetaMoveAdvisor<S, B extends Board<S, M>, M extends Move<S>> implements MoveAdvisor<B, M> {

    protected int maxDepth = 10;//Integer.MAX_VALUE;

    protected boolean isLeaf(B board, S state) {
        return board.getPossibleMoves(state).size() == 0;
    }

    protected abstract int evaluate(B board, S s);

    public Pair<Integer, Optional<M>> alphabeta(B board, S state, int depth, int alpha, int beta, boolean isMaximizing) {

        // base:
        if(depth == 0 || isLeaf(board, state))
            return Pair.of(evaluate(board, state), Optional.empty());

        int bestValue;
        M bestMove = null;

        if(isMaximizing) {
            bestValue = Integer.MIN_VALUE;
            for(M move : board.getPossibleMoves(state))  {
                S child = move.apply(state);
                int result = alphabeta(board, child, depth-1, alpha, beta, !isMaximizing).getLeft();
                alpha = Math.max(alpha, result);
                if( result > bestValue) {
                    bestValue = result;
                    bestMove = move;
                }
                if(beta <= alpha)
                    break;
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for(M move : board.getPossibleMoves(state))  {
                S child = move.apply(state);
                int result = alphabeta(board, child, depth-1, alpha, beta, !isMaximizing).getLeft();
                beta = Math.min(beta, result);
                if( result < bestValue) {
                    bestValue = result;
                    bestMove = move;
                }
                if(beta <= alpha)
                    break;
            }
        }

        return Pair.of(bestValue, Optional.ofNullable(bestMove));
    }

    @Override
    public Optional<M> suggestMove(B board) {
        if(board.getPossibleMoves(board.getState()).size() == 0)
            return Optional.empty();
        S state = board.getState();
        return alphabeta(board, state, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true).getRight();
    }
}
