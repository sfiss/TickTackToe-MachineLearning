package de.fiss.ttt.ai;

import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;
import de.fiss.ttt.model.TreeNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public abstract class MCTSAdvisor<S, B extends Board<S, M>, M extends Move<S>> implements MoveAdvisor<B, M> {

    protected boolean isLeaf(B board, S state) {
        return board.getPossibleMoves(state).size() == 0;
    }

    protected abstract double evaluate(B board, S atState, S resultState);

    @Override
    public Optional<M> suggestMove(B board) {
        TreeNode<S, M> tree = new TreeNode<>(board.getState(), null, null);

        // TODO: make this configurable
        long maxNumberOfIterations = 10000;//System.currentTimeMillis() + (1000 * 1);

        // tree search learning
        mcts(board, tree, maxNumberOfIterations);

        // select best node
        // Note: Taking most simulations played (according to wiki) instead of winratio, score or uct
        // Note: win-ratio also seemed to work!
        Comparator<TreeNode<S, M>> comparator = Comparator.comparing(node ->
                // node.getScore()
                // UCT.uctValue(tree.getVisited(), node.getScore(), node.getVisited())
                //node.getScore() / ((double) node.getVisited())
                node.getVisited()
        );
        TreeNode<S, M> bestNode = tree.getChildren().stream().max(comparator).orElse(tree);

        return Optional.ofNullable(bestNode.getParentMove());
    }

    private void mcts(B board, TreeNode<S, M> tree, long iteration) {
        int count = 0;
        log.debug("Started MCTS");
        while(iteration > count) {//iteration > System.currentTimeMillis()
            count++;
            // selection
            TreeNode<S, M> selectedNode = selection(tree);
            log.debug("Selected " + selectedNode.getState());

            // expansion
            if(!isLeaf(board, selectedNode.getState())) {
                selectedNode.expand(board);
                log.debug("Expanded " + selectedNode.getState());
            }

            // simulation
            TreeNode<S, M> explorableNode = selectedNode.getChildren().stream()
                    .findAny().orElse(selectedNode);
            S rolloutResult = simulation(board, explorableNode);
            log.debug("Simulated " + explorableNode.getState());
            log.debug("Result " + rolloutResult + " = " + evaluate(board, tree.getState(), rolloutResult));

            // backpropagation
            backpropagation(board, explorableNode, rolloutResult);
            log.debug("Backpropagation done");
        }
        //log.info("Tree-visits: " + Arrays.deepToString(tree.getChildren().stream().map(c -> c.getVisited()).toArray()));
        log.debug(String.format("Learning phase over. Executed %d times", count));
    }

    private void backpropagation(B board, TreeNode<S, M> exploredNode, S rolloutResult) {
        if(exploredNode == null)
            return;
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

        if(node.isPresent()) {
            return selection(node.get());
        }

        return parentNode;
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
