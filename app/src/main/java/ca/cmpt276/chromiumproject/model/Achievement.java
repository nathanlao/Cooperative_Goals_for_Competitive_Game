package ca.cmpt276.chromiumproject.model;

/**
 * Achievement contains list of possible achievements you can achieve,
 * as well as potential points for each achievement.
 * Calculates potential points based on current game config's values passed down.
 */
public class Achievement {
    private static final int NUM_ACHIEVEMENTS = 8;

    private String curAchievement;
    private String[] achievementCollection = {"Walking Toddler",
            "Fearless Kid", "HighSchool Star", "District Gang",
            "Infamous King", "Game Emperor",
            "World Conqueror", "Creator of the Game"};
    private int[] potentialAchievePoints = {};
    private int partitionNum = 0;

    private GameConfig curGameConfig;

    public Achievement() {
        this.curAchievement = "Initial Empty";
    }

    public void setCurAchievement(int playerCount, int theScore, GameConfig gameConfig) {
        setPotentialAchievePoint(playerCount, gameConfig);
        int curAchieveEndBoundary = 0;
        for (int i = 0; i < potentialAchievePoints.length; i++) {

            //check on first loop, for Special worst achievement
            if (i == 0) {
                if (theScore <= potentialAchievePoints[0]) {
                    curAchievement = "Toddler's Baby Step";
                }
            }

            curAchieveEndBoundary = potentialAchievePoints[i] + partitionNum;
            //If score is in the Boundary, choose from achievement Collection
            if (theScore >= potentialAchievePoints[i] &&
            theScore < curAchieveEndBoundary) {
                chooseFromAchieveCollection(i);
            }

            //check on last loop, case far larger than expected
            if (i == potentialAchievePoints.length - 1) {
                if (theScore > potentialAchievePoints[i]) {
                    chooseFromAchieveCollection(i);
                }
            }
        }

    }
    public String getCurAchievement() {
        return curAchievement;
    }

    private void chooseFromAchieveCollection(int achieveNum) {
        String theResult = achievementCollection[achieveNum];

        curAchievement = theResult;
    }

    public void setPotentialAchievePoint(int playerCount, GameConfig gameConfig) {
        potentialAchievePoints = new int[NUM_ACHIEVEMENTS];
        curGameConfig = gameConfig;
        int lowestAchieve = playerCount * curGameConfig.getPoorScore();
        int highestAchieve = playerCount * curGameConfig.getGreatScore();

        int middleGround = highestAchieve - lowestAchieve;

        partitionNum = middleGround / NUM_ACHIEVEMENTS;

        //Set initial location for loop
        int partitionMultiplier = 0;
        int curAchieveLocation = 0;

        for (int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            partitionMultiplier = i * partitionNum;

            //Set the Boundary for calculation purpose
            curAchieveLocation = lowestAchieve + partitionMultiplier;

            potentialAchievePoints[i] = curAchieveLocation;
        }
    }
    public int[] getPotentialAchievePoint() {
        return potentialAchievePoints;
    }

    public static Achievement makeAchievement(int playerCount, int theScore, GameConfig gameConfig) {
        Achievement achievement = new Achievement();
        achievement.setCurAchievement(playerCount, theScore, gameConfig);
        return achievement;
    }

}
