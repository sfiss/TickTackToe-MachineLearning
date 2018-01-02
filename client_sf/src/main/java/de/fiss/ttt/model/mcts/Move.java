package de.fiss.ttt.model.mcts;

@FunctionalInterface
public interface Move<S extends State> {
    S apply(S state);
}