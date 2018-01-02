import de.fiss.ttt.GameDTO;
import de.fiss.ttt.ai.MoveAdvisor;
import de.fiss.ttt.ai.RandomMoveAdvisor;
import de.fiss.ttt.ai.mcts.MCTS;
import de.fiss.ttt.ai.tictactoe.TicTacToeMCTS2Adapter;
import de.fiss.ttt.ai.tictactoe.TicTacToeMCTSAdvisor;
import de.fiss.ttt.ai.tictactoe.TicTacToeMinMaxAdvisor;
import de.fiss.ttt.model.Board;
import de.fiss.ttt.model.Move;
import de.fiss.ttt.model.State;
import de.fiss.ttt.model.mcts.Game;
import de.fiss.ttt.model.tictactoe.TTTGame;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

public class AdvisorTest {

    @Test
    public void test_1vs2() {
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> mcts = new TicTacToeMCTSAdvisor();
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> mcts2 = new TicTacToeMCTS2Adapter();
        int[] outcomes = new int[3];
        int count = 0;
        while (count < 30) {
            int outcome = runGame(mcts, mcts2);
            outcomes[outcome]++;
            count++;
        }
        System.out.println(Arrays.toString(outcomes));

    }

    @Test
    public void test_Random_vs_AlphaBeta() {
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> random = new RandomMoveAdvisor<>();
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> mcts = new TicTacToeMCTSAdvisor();
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> minmax = new TicTacToeMinMaxAdvisor();
        int[] outcomes = new int[3];
        int count = 0;
        while (count < 30) {
            int outcome = runGame(random, minmax);
            outcomes[outcome]++;
            count++;
        }
        System.out.println(Arrays.toString(outcomes));

    }

    @Test
    public void test_MCTS_vs_AlphaBeta() {
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> random = new RandomMoveAdvisor<>();
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> mcts = new TicTacToeMCTSAdvisor();
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> minmax = new TicTacToeMinMaxAdvisor();
        int[] outcomes = new int[3];
        int count = 0;
        while (count < 30) {
            int outcome = runGame(mcts, minmax);
            outcomes[outcome]++;
            count++;
        }
        System.out.println(Arrays.toString(outcomes));

    }

    @Test
    public void test_Random_vs_MCTS() {
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> random = new RandomMoveAdvisor<>();
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> mcts = new TicTacToeMCTSAdvisor();
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> minmax = new TicTacToeMinMaxAdvisor();
        int[] outcomes = new int[3];
        int count = 0;
        while (count < 30) {
            int outcome = runGame(random, mcts);
            outcomes[outcome]++;
            count++;
        }
        System.out.println(Arrays.toString(outcomes));

    }

    private int runGame(MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> advisor1, MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> advisor2) {
        int[][] board = {{0,0,0}, {0,0,0}, {0,0,0}};
        int player = 1;
        GameDTO emptyGame = new GameDTO(board, 0,player);
        TicTacToeGame game = new TicTacToeGame(emptyGame);
        TicTacToeGame.State state = game.getState();
        Optional<TicTacToeGame.Move> suggestedMove = advisor1.suggestMove(game);
        while(suggestedMove.isPresent()) {
            state = suggestedMove.get().apply(state);
            game = new TicTacToeGame(state);
            player = 3 - player;
            if(player == 1) {
                suggestedMove = advisor1.suggestMove(game);
            } else {
                suggestedMove = advisor2.suggestMove(game);
            }
        }

        return game.getWinner(state.game);

    }
}
