package ca.cmpt276.chromiumproject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GamePlayed class contains information about a game session.
 * Fields include number of players, their combined score, what achievement they earned, and the date/time it was created
 * GamePlayed class does not know *which* specific game was played.
 */
public class GamePlayed {
    private static final int MIN_PLAYERS = 1;
    private int numPlayers;
    private int combinedScore;
    // TODO: implement actual achievement stuff once it's ready
    private final String achievement;

    private LocalDateTime creationTime;
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("MMM d @ h:mm a");

    public GamePlayed(int numPlayers, int combinedScore) {
        if (numPlayers < MIN_PLAYERS) {
            throw new IllegalArgumentException("Number of players cannot be less than " + MIN_PLAYERS +".");
        }

        achievement = "Testing Turtles";

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
}
