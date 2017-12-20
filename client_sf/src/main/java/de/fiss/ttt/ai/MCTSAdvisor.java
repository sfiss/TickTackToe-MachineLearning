package de.fiss.ttt.ai;

import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;
import de.fiss.ttt.model.TreeNode;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class MCTSAdvisor<S, B extends Board<S, M>, M extends Move<S>> implements MoveAdvisor<B, M> {

    private final int maxNumberOfIterations = 1000;

    protected boolean isLeaf(B board, S state) {
        return board.getPossibleMoves(state).size() == 0;
    }

    protected abstract double evaluate(B board, S atState, S resultState);

    @Override
    public Optional<M> suggestMove(B board) {
        TreeNode<S> tree = new TreeNode<>(board.getState(), null);
        // tree search learning
        mcts(board, tree, maxNumberOfIterations);

        // select best node
        TreeNode<S> bestNode = null;

        return null;
    }

    private void mcts(B board, TreeNode<S> tree, int iteration) {
        while(iteration-- > 0) {
            // selection
            TreeNode<S> selectedNode = selection(tree);

            // expansion
            selectedNode.expand(board);

            // simulation
            TreeNode<S> explorableNode = selectedNode.getChildren().stream()
                    .findAny().orElse(selectedNode);
            S rolloutResult = simulation(board, explorableNode);

            // backpropagation
            backpropagation(board, explorableNode, rolloutResult);
        }
    }

    private void backpropagation(B board, TreeNode<S> exploredNode, S rolloutResult) {
        if(exploredNode == null)
            return;
        // if player is the opponent, then the score is opposite
        double score = evaluate(board, exploredNode.getState(), rolloutResult);
        exploredNode.addScore(score);

        backpropagation(board, exploredNode.getParent(), rolloutResult);
    }

    // TODO: could possibly add depth limit
    // Note: return final state instead of evaluation/rollout result
    private S simulation(B board, TreeNode<S> parentNode) {
        // if leaf: evaluate
        if (isLeaf(board, parentNode.getState()))
            return parentNode.getState();

        // play till end
        // TODO: better move selection
        M selectedMove = // board.getPossibleMoves(parentNode.getState()).stream().findAny().get();
                ((Supplier<M>) () -> {
                    Collection<M> moves = board.getPossibleMoves(parentNode.getState());
                    return new ArrayList<>(moves).get(new Random().nextInt(moves.size()));
                }).get();
        // Note: this will create new detached nodes
        TreeNode<S> selectedNode = new TreeNode<>(selectedMove.apply(parentNode.getState()), null);
        return simulation(board, selectedNode);

    }

    private TreeNode<S> selection(TreeNode<S> parentNode) {
        Optional<TreeNode<S>> node = parentNode.getChildren().stream()
                .max(Comparator.comparing(c ->
                        UCT.uctValue(parentNode.getVisited(), c.getScore(), c.getVisited())));

        return node.orElseGet(() -> parentNode);
    }
}

class UCT {
    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }

        return ((double) nodeWinScore / (double) nodeVisit)
                + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }
}
