package ca.cmpt276.chromiumproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  GameConfig contains information about a type of game (e.g. chess, monopoly)
 *  Fields include name of game, and what constitutes as a PoorScore and GreatScore
 *  Has a list of GamesPlayed. getGamesPlayedData() returns a String array with data about these games
 *  Use setConfigValues() to set its fields
 */

public class GameConfig {
    private String name;
    private int poorScore;
    private int greatScore;
    private List<GamePlayed> gamesPlayed = new ArrayList<>();


    // TODO: might be useful to have empty constructor depending on requirements
    public GameConfig(String name, int poorScore, int greatScore) {
        setConfigValues(name, poorScore, greatScore);
    }

    public void setConfigValues(String name, int poorScore, int greatScore) {
        // TODO: error handling
        this.name = name;
        this.poorScore = poorScore;
        this.greatScore = greatScore;
    }

    public String getName() {
        return name;
    }

    public int getPoorScore() {
        return poorScore;
    }

    public int getGreatScore() {
        return greatScore;
    }

    public void addGamePlayed(GamePlayed gamePlayed) {
        gamesPlayed.add(gamePlayed);
    }

    public int getNumGamesPlayed() {
        return gamesPlayed.size();
    }

    // TODO: need to ask customer if there is a specific data format he wants, for now just reusing format from As2
    public String[] getGamesPlayedData() {
        int numGamesPlayed = getNumGamesPlayed();
        String[] gamesPlayedData;

        if (numGamesPlayed == 0) {
            gamesPlayedData = new String[1];
            gamesPlayedData[0] = "No games";
            return gamesPlayedData;
        }

        gamesPlayedData = new String[numGamesPlayed];
        int itr = 0;
        for (GamePlayed gamePlayed : gamesPlayed) {
            StringBuilder gameData = new StringBuilder();

            // append creation time
            String creationTime = gamePlayed.getCreationTimeString();
            gameData.append(creationTime);

            gameData.append(" - ");

            // append num players
            gameData.append(gamePlayed.getNumPlayers()).append(" players scored ");

            //append combined score
            gameData.append(gamePlayed.getCombinedScore());

            //append achievement
            gameData.append(". Achievement: ").append(gamePlayed.getAchievement());

            gamesPlayedData[itr] = gameData.toString();
            itr++;
        }
        return gamesPlayedData;
    }
}
