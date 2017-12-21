package de.fiss.ttt;

import de.fiss.ttt.ai.MCTSAdvisor;
import de.fiss.ttt.ai.MoveAdvisor;
import de.fiss.ttt.ai.RandomMoveAdvisor;
import de.fiss.ttt.ai.tictactoe.TicTacToeMCTSAdvisor;
import de.fiss.ttt.ai.tictactoe.TicTacToeMinMaxAdvisor;
import de.fiss.ttt.model.tictactoe.TicTacToeGame;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


public class Application {

    // TODO: parse player etc via commandLine (library), handle exception
    public static void main(String... args) throws Exception {
        MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> advisor;
        switch (args[0]) {
            case "random":
                advisor = new RandomMoveAdvisor<>();
                break;
            case "minmax":
                advisor = new TicTacToeMinMaxAdvisor();
                break;
            case "mcts":
                advisor = new TicTacToeMCTSAdvisor();
                break;
            default:
                throw new RuntimeException("No such advisor");
        }

        int player = Integer.parseInt(args[1]);

        RestTemplate restTemplate = new RestTemplate();

        while (true) {
            GameDTO game = restTemplate.getForObject("http://localhost:8080/game/status", GameDTO.class);

            if (game.getToPlay() != player) {
                Thread.sleep(100);
                continue;
            }

            TicTacToeGame ttt = new TicTacToeGame(game);
            Optional<TicTacToeGame.Move> suggestedMove = advisor.suggestMove(ttt);
            if (suggestedMove.isPresent()) {
                TicTacToeGame.Move move = suggestedMove.get();
                System.out.println("State " + ttt.getState());
                System.out.println("Move " + move);
                restTemplate.postForLocation(String.format("http://localhost:8080/game/play?row=%d&column=%d&player=%d",
                        move.row, move.col, move.value), null);
            } else {
                throw new RuntimeException("Can not perform move");
            }
        }
    }
}
