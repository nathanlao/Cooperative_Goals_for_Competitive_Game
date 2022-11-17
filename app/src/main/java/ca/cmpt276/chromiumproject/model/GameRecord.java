package ca.cmpt276.chromiumproject.model;

import static ca.cmpt276.chromiumproject.model.Achievement.makeScaledAchievement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GameRecord stores contains information about a game session that has been played
 * Knows number of players, their combined score, what achievement they earned, difficulty of the game, and the date/time it was created
 * GameRecord's constructor needs to know poorScore and greatScore of the GameConfig that was played in order to calculate its achievement.
 */
public class GameRecord {
    private static final int MIN_PLAYERS = 1;

    private int numPlayers;
    private int combinedScore;

    private int achievementLevel;
    private String creationTime;

    private Difficulty difficulty;

    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("MMM d @ h:mm a");

    public GameRecord(int numPlayers, int combinedScore, int poorScore, int greatScore, Difficulty difficulty) {
        if (numPlayers < MIN_PLAYERS) {
            throw new IllegalArgumentException("Number of players cannot be less than " + MIN_PLAYERS +".");
        }

        this.numPlayers = numPlayers;
        this.combinedScore = combinedScore;
        this.difficulty = difficulty;

        creationTime = LocalDateTime.now().format(DT_FORMAT);
        Achievement theAchievement = makeScaledAchievement(numPlayers, combinedScore, poorScore, greatScore, difficulty);
        this.achievementLevel  = theAchievement.getCurAchievementLevel();
    }

    // TODO: If you want to get difficulty as a string, please refer to Difficulty class comments :]
    public Difficulty getDifficulty() {
        return difficulty;
    }

    // TODO: please use this to edit GameRecord values!! :]
    public void editGameRecordValues(int numPlayers, int combinedScore, int poorScore, int greatScore, Difficulty difficulty) {
        this.numPlayers = numPlayers;
        this.combinedScore = combinedScore;
        this.difficulty = difficulty;

        // recalculate the achievement level based on the new difficulty
        Achievement theAchievement = makeScaledAchievement(numPlayers, combinedScore, poorScore, greatScore, difficulty);
        this.achievementLevel = theAchievement.getCurAchievementLevel();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getCombinedScore() {
        return combinedScore;
    }

    public String getCreationTimeString() {
        return creationTime;
    }

    public int getAchievementLevel() {
        return achievementLevel;
    }
}
