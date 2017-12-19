package de.fiss.ttt;

public class GameDTO {
    private final int[][] game;
    private final int winner;
    private final int toPlay;

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
}
