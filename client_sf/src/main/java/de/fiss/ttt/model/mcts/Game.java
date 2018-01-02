package de.fiss.ttt.model.mcts;

import de.fiss.ttt.model.mcts.Move;

import java.util.Collection;

public abstract class Game<S extends State, M extends Move<S>> {
    public abstract boolean isFinished(S state);

    public abstract Collection<M> getPossibleMoves(S state);

    public abstract Player getWinner(S state);

    public static enum Player {
        NONE, ONE, TWO;

        public Player toggle() {
            switch (this) {
                case ONE:
                    return TWO;
                case TWO:
                    return ONE;
                default:
                    return NONE;
            }
        }
    }
}
