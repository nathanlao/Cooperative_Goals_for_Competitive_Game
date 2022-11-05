package ca.cmpt276.chromiumproject.model;

import static ca.cmpt276.chromiumproject.model.Achievement.makeAchievement;

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

    private Achievement theAchievement;
    private LocalDateTime creationTime;
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("MMM d @ h:mm a");

    // TODO: Could handle achievement object in a cleaner way (Pass in achievement object for now to avoid NULL OBJECT ERROR)
    public GameRecord(int numPlayers, int combinedScore, GameConfig gameConfig) {
        if (numPlayers < MIN_PLAYERS) {
            throw new IllegalArgumentException("Number of players cannot be less than " + MIN_PLAYERS +".");
        }

        this.numPlayers = numPlayers;
        this.combinedScore = combinedScore;
        this.creationTime = LocalDateTime.now();
        this.theAchievement = makeAchievement(numPlayers, combinedScore, gameConfig);
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
        return theAchievement.getCurAchievement();
    }

    public void calcAchievement(int numPlayers, int combinedScore, GameConfig gameConfig) {
        theAchievement.setCurAchievement(numPlayers, combinedScore, gameConfig);
    }
}
