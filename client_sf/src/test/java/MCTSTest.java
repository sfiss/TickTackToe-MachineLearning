import de.fiss.ttt.GameDTO;
import de.fiss.ttt.ai.tictactoe.TicTacToeMCTSAdvisor;
import de.fiss.ttt.ai.tictactoe.TicTacToeMinMaxAdvisor;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MCTSTest {

    @Test
    public void testSimulation() {
        int[][] gameState = {{2,0,0}, {0,1,1}, {0,0,0}};
        GameDTO state = new GameDTO(gameState, 0, 2);
        TicTacToeGame game = new TicTacToeGame(state);
        TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
        advisor.suggestMove(game);
    }
}
