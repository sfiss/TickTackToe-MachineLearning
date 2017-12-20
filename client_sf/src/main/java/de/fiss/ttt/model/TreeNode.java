package de.fiss.ttt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class TreeNode<S> {
    private final S state;
    private final TreeNode<S> parent;
    private Collection<TreeNode<S>> children = new ArrayList<>();
    private int visited = 0;
    private double score = 0.0;


    public Collection<TreeNode<S>> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    public <B extends Board<S, M>, M extends Move<S>> void expand(B board) {
        children = board.getPossibleMoves(state).stream()
                .map(m -> m.apply(state))
                .map(s -> new TreeNode<>(s, this))
                .collect(Collectors.toList());
    }

    public void addScore(double score) {
        visited++;
        this.score += score;
    }
}
