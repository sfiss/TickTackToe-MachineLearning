package de.fiss.ttt;

import org.springframework.web.bind.annotation.*;

@RestController
public class TTTController {

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help() {
        return "help";
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public String game() {
        return "Game should be created";
    }

    @RequestMapping(value = "/game/status", method = RequestMethod.GET)
    public GameDTO status() {
        return Game.getGame();
    }

    @RequestMapping(value = "/game/play", method = RequestMethod.POST)
    public String play(@RequestParam int row, @RequestParam int column, @RequestParam int player) {
        assert row > 0 && column >= 0 && (player == 1 || player == 2);
        Game.play(row, column, player);
        return "OK";
    }

}
