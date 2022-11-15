package ca.cmpt276.chromiumproject.model;

/**
 * Achievement contains list of possible achievements you can achieve,
 * as well as potential points for each achievement.
 * Calculates potential points based on current game config's values passed down.
 */
public class Achievement {
    private int achievementLevel;

    private static final int NUM_ACHIEVEMENTS = 8;

    private static final double EASY_SCALE_FACTOR = 0.75;
    private static final double HARD_SCALE_FACTOR = 1.25;

    private int[] potentialAchievePoints = {};
    private int partitionNum = 0;

    public static final int SPECIAL_WORST_ACHIEVE = -1;

    public Achievement() {
        this.achievementLevel = 0;
    }

    public static Achievement makeAchievement(int playerCount, int theScore, int poorScore, int greatScore) {
        Achievement achievement = new Achievement();
        int achievementLevel = achievement.calculateAchievementLevel(playerCount, theScore, poorScore, greatScore);
        achievement.setAchievementLevel(achievementLevel);
        return achievement;
    }

    private void setAchievementLevel(int achievementLevel) {
        this.achievementLevel = achievementLevel;
    }

    public int getCurAchievementLevel() {
        return achievementLevel;
    }

    public void setPotentialAchievePoint(int playerCount, int poorScore, int greatScore) {
        potentialAchievePoints = new int[NUM_ACHIEVEMENTS];

        int lowestAchieve = playerCount * poorScore;
        int highestAchieve = playerCount * greatScore;

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

    // Calculates potential achievement points given playerCount and gameConfig, removing the need to explicitly declare a collective score.
    // Useful for showing list of possible scores.
    public static int[] getStaticPotentialAchievePoint(int playerCount, GameConfig gameConfig) {
        int potentialScore = 0;
        int poorScore = gameConfig.getPoorScore();
        int greatScore = gameConfig.getGreatScore();
        Achievement potentialAchievement = makeAchievement(playerCount, potentialScore, poorScore, greatScore);
        potentialAchievement.setPotentialAchievePoint(playerCount, poorScore, greatScore);
        return potentialAchievement.getPotentialAchievePoint();
    }

    public int[] getPotentialAchievePoint() {
        return potentialAchievePoints;
    }

    // Calculates Achievement Level without considering difficulty.
    private int calculateAchievementLevel(int playerCount, int theScore, int poorScore, int greatScore) {
        int achievementLevel = 0;

        setPotentialAchievePoint(playerCount, poorScore, greatScore);

        int curAchieveEndBoundary = 0;
        for (int i = 0; i < potentialAchievePoints.length; i++) {

            //check on first loop, for Special worst achievement
            if (i == 0) {
                if (theScore <= potentialAchievePoints[0]) {
                    achievementLevel = SPECIAL_WORST_ACHIEVE;
                }
            }

            curAchieveEndBoundary = potentialAchievePoints[i] + partitionNum;
            //If score is in the Boundary, choose from achievement Collection
            if (theScore >= potentialAchievePoints[i] &&
                    theScore < curAchieveEndBoundary) {
                achievementLevel = i;
            }

            //check on last loop, case far larger than expected
            if (i == potentialAchievePoints.length - 1) {
                if (theScore > potentialAchievePoints[i]) {
                    achievementLevel = i;
                }
            }
        }

        return achievementLevel;
    }

}
