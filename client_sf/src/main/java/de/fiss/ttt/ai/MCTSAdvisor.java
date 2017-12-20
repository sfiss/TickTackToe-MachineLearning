package de.fiss.ttt.ai;

import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;
import de.fiss.ttt.model.TreeNode;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MCTSAdvisor<S, B extends Board<S, M>, M extends Move<S>> implements MoveAdvisor<B, M> {

    protected boolean isLeaf(B board, S state) {
        return board.getPossibleMoves(state).size() == 0;
    }

    protected abstract double evaluate(B board, S atState, S resultState);

    @Override
    public Optional<M> suggestMove(B board) {
        TreeNode<S, M> tree = new TreeNode<>(board.getState(), null, null);
        long maxNumberOfIterations = System.currentTimeMillis() + (1000 * 1);
        // tree search learning
        mcts(board, tree, maxNumberOfIterations);

        // select best node (note: should take the minimal node, as opponent plays next)
        TreeNode<S, M> bestNode = tree.getChildren().stream().min(Comparator.comparing(node -> node.getScore())).orElse(tree);

        return Optional.ofNullable(bestNode.getParentMove());
    }

    private void mcts(B board, TreeNode<S, M> tree, long iteration) {
        int count = 0;
        while(iteration > System.currentTimeMillis()) {
            count++;
            System.out.println("STARTED ITERATION");
            print(tree, 0);
            // selection
            TreeNode<S, M> selectedNode = selection(tree);
            System.out.println("SELECT " + selectedNode.getState());

            // expansion
            selectedNode.expand(board);
            System.out.println("EXPAND " + selectedNode.getState());

            // simulation
            TreeNode<S, M> explorableNode = selectedNode.getChildren().stream()
                    .findAny().orElse(selectedNode);
            S rolloutResult = simulation(board, explorableNode);
            System.out.println("EXPLORE " + explorableNode.getState());
            System.out.println("RESULT " + rolloutResult);

            // backpropagation
            backpropagation(board, explorableNode, rolloutResult);

            // debug: print tree
            System.out.println("AFTER ITERATION: ");
            print(tree, 0);
        }
        System.out.println("Learned " + count + " times :)");
    }

    private void print(TreeNode<S, M> tree, int level) {
        Function<TreeNode<S, M>, String> formatter = (node) -> tree.getState().toString() + " (" + tree.getVisited() + "/" + tree.getScore() + ")";
        System.out.println(String.join("",IntStream.range(0, level).mapToObj(i -> "-").collect(Collectors.toList())) + formatter.apply(tree));
        tree.getChildren().forEach(n -> print(n, level+1));
    }

    private void backpropagation(B board, TreeNode<S, M> exploredNode, S rolloutResult) {
        if(exploredNode == null)
            return;
        // if player is the opponent, then the score is opposite
        double score = evaluate(board, exploredNode.getState(), rolloutResult);
        exploredNode.addScore(score);

        backpropagation(board, exploredNode.getParent(), rolloutResult);
    }

    // TODO: could possibly add depth limit
    // Note: return final state instead of evaluation/rollout result
    private S simulation(B board, TreeNode<S, M> parentNode) {
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
        TreeNode<S, M> selectedNode = new TreeNode<>(selectedMove.apply(parentNode.getState()), selectedMove, null);
        return simulation(board, selectedNode);

    }

    private TreeNode<S, M> selection(TreeNode<S, M> parentNode) {
        Optional<TreeNode<S, M>> node = parentNode.getChildren().stream()
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
