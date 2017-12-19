package de.fiss.ttt;

public class GameDTO {
    private final int[][] game;

    public GameDTO(int[][] game) {
        this.game = game;
    }

    public int[][] getGame() {
        return game;
    }
}
