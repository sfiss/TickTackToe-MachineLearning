import asdasd.Board;
import asdasd.MonteCarloTreeSearch;
import asdasd.Tree;
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
    public void testCornersASDTimes1000() {
        int[] wrong = new int[4];
        int[][][] gameStates = {
                {{1,0,0}, {0,0,0}, {0,0,0}},
                {{0,0,1}, {0,0,0}, {0,0,0}},
                {{0,0,0}, {0,0,0}, {1,0,0}},
                {{0,0,0}, {0,0,0}, {0,0,1}}
        };
        for(int i = 0; i != 100; i++) {
            for (int j = 0; j != 4; j++) {
                MonteCarloTreeSearch search = new MonteCarloTreeSearch();

                if (!Arrays.equals(
                        Arrays.stream(search.findNextMove(new Board(gameStates[j]), 2).getBoardValues())
                                .flatMap(x -> Arrays.stream(x).boxed()).mapToInt(x -> x).toArray(),
                        Arrays.stream(new TicTacToeGame.Move(1, 1, 2).apply(new TicTacToeGame.State(gameStates[j], 2)).game)
                                .flatMap(x -> Arrays.stream(x).boxed()).mapToInt(x -> x).toArray()))
                    wrong[j]++;
            }
        }
        System.out.println("WAS WRONG 2 " + Arrays.toString(wrong));
    }

    @Test
    public void testCornersFGFGTimes1000() {
        int[] wrong = new int[4];
        int[][][] gameStates = {
                {{1,0,0}, {0,0,0}, {0,0,0}},
                {{0,0,1}, {0,0,0}, {0,0,0}},
                {{0,0,0}, {0,0,0}, {1,0,0}},
                {{0,0,0}, {0,0,0}, {0,0,1}}
        };
        for(int i = 0; i != 100; i++) {
            for (int j = 0; j != 4; j++) {
                fgfgfgf.MonteCarloTreeSearch search = new fgfgfgf.MonteCarloTreeSearch();

                if (!Arrays.equals(
                        Arrays.stream(search.findNextMove(new fgfgfgf.Board(gameStates[j]), 2).getBoardValues())
                                .flatMap(x -> Arrays.stream(x).boxed()).mapToInt(x -> x).toArray(),
                        Arrays.stream(new TicTacToeGame.Move(1, 1, 2).apply(new TicTacToeGame.State(gameStates[j], 2)).game)
                                .flatMap(x -> Arrays.stream(x).boxed()).mapToInt(x -> x).toArray()))
                    wrong[j]++;
            }
        }
        System.out.println("WAS WRONG 2 " + Arrays.toString(wrong));
    }

    @Test
    public void testCornersTimes1000() {
        int[] wrong = new int[4];
        int[][][] gameStates = {
                {{1,0,0}, {0,0,0}, {0,0,0}},
                {{0,0,1}, {0,0,0}, {0,0,0}},
                {{0,0,0}, {0,0,0}, {1,0,0}},
                {{0,0,0}, {0,0,0}, {0,0,1}}
        };
        for(int i = 0; i != 100; i++) {
            for (int j = 0; j != 4; j++) {
                GameDTO state = new GameDTO(gameStates[j], 0, 2);
                TicTacToeGame.Move move = new TicTacToeGame.Move(1, 1, 2);
                TicTacToeGame game = new TicTacToeGame(state);
                TicTacToeMCTSAdvisor advisor = new TicTacToeMCTSAdvisor();
                if (!move.equals(advisor.suggestMove(game).get()))
                    wrong[j]++;
            }
        }
        System.out.println("WAS WRONG " + Arrays.toString(wrong));
    }

    @Test
    public void testCornersNewTimes1000() {
        int[] wrong = new int[4];
        int[][][] gameStates = {
                {{1,0,0}, {0,0,0}, {0,0,0}},
                {{0,0,1}, {0,0,0}, {0,0,0}},
                {{0,0,0}, {0,0,0}, {1,0,0}},
                {{0,0,0}, {0,0,0}, {0,0,1}}
        };
        for(int i = 0; i != 100; i++) {
            for (int j = 0; j != 4; j++) {
                TTTGame.TTTState state = new TTTGame.TTTState(gameStates[j], 2);
                TTTGame.TTTMove move = new TTTGame.TTTMove(1, 1, 2);
                MCTS<TTTGame.TTTState, TTTGame.TTTMove, TTTGame> advisor = new MCTS<>();
                TTTGame.TTTMove suggestedMove = advisor.suggestMove(new TTTGame(), state, Game.Player.TWO).get();
                if (! (move.row == suggestedMove.row && move.col == suggestedMove.col && move.value == suggestedMove.value))
                    wrong[j]++;
            }
        }
        System.out.println("WAS WRONG " + Arrays.toString(wrong));
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
