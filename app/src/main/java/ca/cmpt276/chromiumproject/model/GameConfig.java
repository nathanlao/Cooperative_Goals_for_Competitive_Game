package ca.cmpt276.chromiumproject.model;

import java.util.ArrayList;
import java.util.List;

public class GameConfig {
    private String name;
    private int poorScore;
    private int greatScore;
    private List<GamePlayed> gamesPlayed = new ArrayList<>();

    public GameConfig(String name, int poorScore, int greatScore) {
        this.name = name;
        this.poorScore = poorScore;
        this.greatScore = greatScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoorScore() {
        return poorScore;
    }

    public void setPoorScore(int poorScore) {
        this.poorScore = poorScore;
    }

    public int getGreatScore() {
        return greatScore;
    }

    public void setGreatScore(int greatScore) {
        this.greatScore = greatScore;
    }

    public List<GamePlayed> getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(List<GamePlayed> gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
}
