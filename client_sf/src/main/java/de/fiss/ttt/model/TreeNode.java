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
public class TreeNode<S, M extends Move<S>> {
    private final S state;
    private final M parentMove;
    private final TreeNode<S, M> parent;
    private Collection<TreeNode<S, M>> children = new ArrayList<>();
    private int visited = 0;
    private double score = 0.0;


    public Collection<TreeNode<S, M>> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    public <B extends Board<S, M>> void expand(B board) {
        children = board.getPossibleMoves(state).stream()
                .map(m -> new TreeNode<S, M>(m.apply(state), m,this))
                .collect(Collectors.toList());
    }

    public void addScore(double score) {
        visited++;
        this.score += score;
    }
}
