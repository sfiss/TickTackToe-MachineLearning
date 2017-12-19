import de.fiss.ttt.GameDTO;
import de.fiss.ttt.ai.TicTacToeMinMaxAdvisor;
import de.fiss.ttt.model.TicTacToeGame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimaxTest {

    @Test
    public void testMinimax() {
        int[][] gameState = {{2,0,0}, {0,1,1}, {0,0,0}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMinMaxAdvisor advisor = new TicTacToeMinMaxAdvisor();
        assertEquals(new TicTacToeGame.Move(1,0,2), advisor.suggestMove(game).get());
    }

    @Test
    public void testSemiFullMinimax() {
        int[][] gameState = {{2,2,1}, {2,2,0}, {1,1,0}};
        GameDTO state = new GameDTO(gameState, 0, 1);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMinMaxAdvisor advisor = new TicTacToeMinMaxAdvisor();
        assertEquals(new TicTacToeGame.Move(2,2,1), advisor.suggestMove(game).get());
    }

    @Test
    public void testFullMinimax() {
        int[][] gameState = {{2,2,0}, {1,1,2}, {1,1,2}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMinMaxAdvisor advisor = new TicTacToeMinMaxAdvisor();
        assertEquals(new TicTacToeGame.Move(0,2,2), advisor.suggestMove(game).get());
    }
}
