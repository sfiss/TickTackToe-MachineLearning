package de.fiss.ttt.model;

@FunctionalInterface
public interface Move<BoardState> {
    BoardState apply(BoardState state);
}
