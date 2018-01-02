import de.fiss.ttt.GameDTO;
import de.fiss.ttt.ai.mcts.MCTS;
import de.fiss.ttt.ai.tictactoe.TicTacToeMCTSAdvisor;
import de.fiss.ttt.ai.tictactoe.TicTacToeMinMaxAdvisor;
import de.fiss.ttt.model.mcts.Game;
import de.fiss.ttt.model.tictactoe.TTTGame;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MCTSTest {

    @Test
    public void testCorner1() {
        int[][] gameState = {{1,0,0}, {0,0,0}, {0,0,0}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame.Move move = new TicTacToeGame.Move(1,1,2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
        assertEquals(move, advisor.suggestMove(game).get());
    }

    @Test
    public void testCorner2() {
        int[][] gameState = {{0,0,1}, {0,0,0}, {0,0,0}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame.Move move = new TicTacToeGame.Move(1,1,2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
        assertEquals(move, advisor.suggestMove(game).get());
    }

    @Test
    public void testCorner3() {
        int[][] gameState = {{0,0,0}, {0,0,0}, {0,0,1}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame.Move move = new TicTacToeGame.Move(1,1,2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
        assertEquals(move, advisor.suggestMove(game).get());
    }

    @Test
    public void testCorner4() {
        int[][] gameState = {{0,0,0}, {0,0,0}, {1,0,0}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame.Move move = new TicTacToeGame.Move(1,1,2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
        assertEquals(move, advisor.suggestMove(game).get());
    }

    @Test
    public void testSmall() {
        int[][] gameState = {{0,1,1}, {0,2,1}, {2,2,0}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame.Move move = new TicTacToeGame.Move(2,2,2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
        assertEquals(move, advisor.suggestMove(game).get());
    }

    @Test
    public void testSimulation() {
        int[][] gameState = {{0,0,1}, {2,2,1}, {1,0,0}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame.Move move = new TicTacToeGame.Move(2,2,2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
        assertEquals(move, advisor.suggestMove(game).get());

    }
}
