package ca.cmpt276.chromiumproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GameRecord stores contains information about a game session that has been played
 * Knows number of players, their combined score, what achievement they earned, and the date/time it was created
 * GameRecord's constructor needs to know the GameConfig that was played in order to calculate its achievement.
 */
public class GameRecord {
    private static final int MIN_PLAYERS = 1;

    private int numPlayers;
    private int combinedScore;

    private Achievement theAchivement;
    private final String achievement = theAchivement.getCurAchievement();


    private GameConfig gameConfig; // TODO: may not need this as an instance variable if Achievement can be calculated in the constructor
    private LocalDateTime creationTime;
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("MMM d @ h:mm a");

    public GameRecord(int numPlayers, int combinedScore, GameConfig gameConfig) {
        if (numPlayers < MIN_PLAYERS) {
            throw new IllegalArgumentException("Number of players cannot be less than " + MIN_PLAYERS +".");
        }

        this.numPlayers = numPlayers;
        this.combinedScore = combinedScore;
        this.gameConfig = gameConfig;
        this.creationTime = LocalDateTime.now();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getCombinedScore() {
        return combinedScore;
    }

    public String getCreationTimeString() {
        return creationTime.format(DT_FORMAT);
    }

    public String getAchievement() {
        return achievement;
    }

    public void calcAchivement() {
        theAchivement.setCurAchievement(numPlayers, combinedScore);
    }
}
