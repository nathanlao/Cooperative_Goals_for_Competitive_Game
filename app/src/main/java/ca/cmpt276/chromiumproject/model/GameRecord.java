package ca.cmpt276.chromiumproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GameRecord stores contains information about a game session that has been played
 * Knows number of players, their combined score, what achievement they earned, and the date/time it was created
 * GameRecord does not know *which* specific game was played.
 */
public class GameRecord {
    private static final int MIN_PLAYERS = 1;

    private int numPlayers;
    private int combinedScore;

    private Achievement theAchivement;
    private final String achievement = theAchivement.getCurAchievement();


    private LocalDateTime creationTime;
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("MMM d @ h:mm a");

    public GameRecord(int numPlayers, int combinedScore) {
        if (numPlayers < MIN_PLAYERS) {
            throw new IllegalArgumentException("Number of players cannot be less than " + MIN_PLAYERS +".");
        }

        //achievement = "Testing Turtles";

        this.numPlayers = numPlayers;
        this.combinedScore = combinedScore;
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
