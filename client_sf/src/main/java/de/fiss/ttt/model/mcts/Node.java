package de.fiss.ttt.model.mcts;

import de.fiss.ttt.model.TreeNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Node<S extends State, M extends Move<S>> {
    private final Game.Player owner;
    private final S state;
    private final M parentMove;
    private final Node<S, M> parent;
    private List<Node<S, M>> children = new ArrayList<>();
    private int visited = 0;
    private double score = 0.0;

    public void expand(Game<S, M> game) {
        this.children.addAll(
                game.getPossibleMoves(state).stream().map(move ->
                    new Node<S, M>(owner.toggle(), move.apply(state), move, this)
                ).collect(Collectors.toList()));
    }

    public Node<S, M> createChildByMove(M move) {
        return new Node<>(owner.toggle(), move.apply(state), move, this);
    }

    public void addScore(double score) {
        visited++;
        this.score += score;
    }
}
