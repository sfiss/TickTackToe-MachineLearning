import de.fiss.ttt.GameDTO;
import de.fiss.ttt.ai.tictactoe.TicTacToeMCTSAdvisor;
import de.fiss.ttt.ai.tictactoe.TicTacToeMinMaxAdvisor;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MCTSTest {

    @Test
    public void testCorner() {
        int[][] gameState = {{0,0,1}, {0,0,0}, {0,0,0}};
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
