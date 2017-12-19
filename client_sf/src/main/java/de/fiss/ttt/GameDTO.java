package de.fiss.ttt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDTO implements Cloneable {
    private int[][] game;
    private int winner;
    private int toPlay;

    public GameDTO() {}

    public GameDTO(int[][] game, int winner, int toPlay) {
        this.game = game;
        this.winner = winner;
        this.toPlay = toPlay;
    }

    public int[][] getGame() {
        return game;
    }

    public int getWinner() {
        return winner;
    }

    public int getToPlay() {
        return toPlay;
    }

    public void setGame(int[][] game) {
        this.game = game;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public void setToPlay(int toPlay) {
        this.toPlay = toPlay;
    }
}
