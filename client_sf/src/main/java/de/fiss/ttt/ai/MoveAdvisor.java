package de.fiss.ttt.ai;

import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MoveAdvisor<B extends Board, M extends Move> {
    Optional<M> suggestMove(B board);
}
