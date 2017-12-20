package de.fiss.ttt.model;

import java.util.Collection;

public interface Board<S, M extends Move<S>> {
    Collection<M> getPossibleMoves(S state);

    S getState();
}
