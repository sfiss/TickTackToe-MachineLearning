package de.fiss.ttt.ai.mcts;

import de.fiss.ttt.model.mcts.Move;
import de.fiss.ttt.model.mcts.Game;
import de.fiss.ttt.model.mcts.Node;
import de.fiss.ttt.model.mcts.State;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.function.Supplier;

@Log4j2
public class MCTS<S extends State, M extends Move<S>, G extends Game<S, M>> {

    public Optional<M> suggestMove(G game, S state, Game.Player player) {
        Node<S, M> tree = new Node<>(player.toggle(), state, null, null);

        // TODO: make this configurable
        long maxNumberOfIterations = System.currentTimeMillis() + (1000 * 1);

        // tree search learning
        mcts(game, tree, maxNumberOfIterations);

        // select best node
        Comparator<Node<S, M>> bestNodeSelector = Comparator.comparing(node ->
                node.getVisited()
        );
        Node<S, M> bestNode = tree.getChildren().stream().max(bestNodeSelector).orElse(tree);

        return Optional.ofNullable(bestNode.getParentMove());
    }

    private void mcts(G game, Node<S, M> tree, long iteration) {
        int count = 0;
        while(iteration > System.currentTimeMillis()) {
            count++;

            // selection
            Node<S, M> selectedNode = selectNode(tree);

            // expansion
            if(!game.isFinished(selectedNode.getState())) {
                expandNode(game, selectedNode);
            }

            // simulation
            // TODO: smart child selection?
            Node<S, M> explorableNode = selectedNode.getChildren().stream()
                    .findAny().orElse(selectedNode);
            Game.Player rolloutResult = simulation(game, explorableNode);

            // backpropagation
            backpropagation(game, explorableNode, rolloutResult);
        }
        log.debug(String.format("Learning phase over. Executed %d times", count));
    }

    private void expandNode(G game, Node<S, M> selectedNode) {
        selectedNode.expand(game);
    }

    private void backpropagation(G game, Node<S, M> exploredNode, Game.Player winner) {
        if(exploredNode == null)
            return;
        double score = winner == Game.Player.NONE ?
                0 : (winner == exploredNode.getOwner() ? 1 : -1);
        exploredNode.addScore(score);

        backpropagation(game, exploredNode.getParent(), winner);
    }

    // TODO: could possibly add depth limit
    // Note: return winner of final state
    private Game.Player simulation(G game, Node<S, M> parentNode) {
        // if leaf: evaluate
        if (game.isFinished(parentNode.getState()))
            return game.getWinner(parentNode.getState());

        // play till end
        // TODO: better Move selection via heuristic
        M selectedMove =
                ((Supplier<M>) () -> {
                    Collection<M> moves = game.getPossibleMoves(parentNode.getState());
                    return new ArrayList<>(moves).get(new Random().nextInt(moves.size()));
                }).get();

        Node<S, M> selectedNode = parentNode.createChildByMove(selectedMove);
        return simulation(game, selectedNode);

    }

    private Node<S, M> selectNode(Node<S, M> parentNode) {
        Optional<Node<S, M>> node = parentNode.getChildren().stream()
                .max(Comparator.comparing(c ->
                        UCT.uctValue(parentNode.getVisited(), c.getScore(), c.getVisited())));

        if(node.isPresent()) {
            return selectNode(node.get());
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
