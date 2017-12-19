package de.fiss.ttt;

import de.fiss.ttt.ai.MoveAdvisor;
import de.fiss.ttt.ai.RandomMoveAdvisor;
import de.fiss.ttt.ai.TicTacToeMinMaxAdvisor;
import de.fiss.ttt.model.TicTacToeGame;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


public class Application {
    static MoveAdvisor<TicTacToeGame, TicTacToeGame.Move> advisor
            = new TicTacToeMinMaxAdvisor();
      //  = new RandomMoveAdvisor<>();

    static int player = 2;

    // TODO: parse player etc via commandLine (library), handle exception
    public static void main(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        while(true) {
            GameDTO game = restTemplate.getForObject("http://localhost:8080/game/status", GameDTO.class);

            if(game.getToPlay() != player) {
                Thread.sleep(100);
                continue;
            }

            Optional<TicTacToeGame.Move> suggestedMove = advisor.suggestMove(new TicTacToeGame(game));
            if(suggestedMove.isPresent()) {
                // TODO: perform move
                TicTacToeGame.Move move = suggestedMove.get();
                restTemplate.postForLocation(String.format("http://localhost:8080/game/play?row=%d&column=%d&player=%d",
                        move.row, move.col, move.value), null);
            } else {
                throw new RuntimeException("Can not perform move");
            }
        }
    }
}
