package de.fiss.ttt.model;

@FunctionalInterface
public interface Move<S> {
    S apply(S state);
}
