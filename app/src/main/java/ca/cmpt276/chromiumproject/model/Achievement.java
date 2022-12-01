package ca.cmpt276.chromiumproject.model;

/**
 * Achievement contains list of possible achievements you can achieve,
 * as well as potential points for each achievement.
 * Calculates potential points based on current game config's values passed down.
 * normalAchievePoints stores the scores required to earn an achievement level at NORMAL difficulty.
 */

public class Achievement {
    private int achievementLevel;

    private static final int NUM_ACHIEVEMENTS = 8;

    private static final double EASY_SCALE_FACTOR = 0.75;
    private static final double HARD_SCALE_FACTOR = 1.25;

    private int[] normalAchievePoints;
    private int partitionNum = 0;

    public static final int SPECIAL_WORST_ACHIEVE = -1;

    // default constructor makes an Achievement at NORMAL difficulty -> scores required to earn an achievement level is 100% of the usual amount
    public Achievement(int playerCount, int theScore, int poorScore, int greatScore) {
        normalAchievePoints = calculateNormalAchievePoints(playerCount, poorScore, greatScore);
        achievementLevel = calculateAchievementLevel(normalAchievePoints, theScore);
    }

    public static Achievement makeScaledAchievement(int playerCount, int theScore, int poorScore, int greatScore, Difficulty difficulty) {
        Achievement achievement = new Achievement(playerCount, theScore, poorScore, greatScore);

        if (difficulty != Difficulty.NORMAL) {
            int[] scaledAchievePoints = achievement.scaleAchievePointsToDifficulty(achievement.getNormalAchievePoints(), difficulty);
            int scaledAchievementLevel = achievement.calculateAchievementLevel(scaledAchievePoints, theScore);

            achievement.setAchievementLevel(scaledAchievementLevel);
        }

        return achievement;
    }

    public int getCurAchievementLevel() {
        return achievementLevel;
    }

    // Calculates potential achievement points given playerCount, gameConfig, and difficulty. Removes the need to explicitly declare a collective score.
    // Useful for showing list of possible scores for a specified difficulty.
    public static int[] getStaticAchievePointsByDifficulty(int playerCount, GameConfig gameConfig, Difficulty difficulty) {
        int potentialScore = 0;
        int poorScore = gameConfig.getPoorScore();
        int greatScore = gameConfig.getGreatScore();
        Achievement potentialAchievement = makeScaledAchievement(playerCount, potentialScore, poorScore, greatScore, difficulty);
        int[] normalAchievePoints = potentialAchievement.getNormalAchievePoints();

        return potentialAchievement.scaleAchievePointsToDifficulty(normalAchievePoints, difficulty);
    }

    private int[] getNormalAchievePoints() {
        return normalAchievePoints;
    }

    // calculates and returns achievement point thresholds according to NORMAL difficulty, without any difficulty scaling.
    private int[] calculateNormalAchievePoints(int playerCount, int poorScore, int greatScore) {
        int[] normalAchievePoints = new int[NUM_ACHIEVEMENTS];

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

            normalAchievePoints[i] = curAchieveLocation;
        }

        return normalAchievePoints;
    }

    // scales NORMAL-level achievePoints to specified difficulty. returns int[] with scaled point thresholds.
    private int[] scaleAchievePointsToDifficulty(int[] normalAchievePoints, Difficulty difficulty) {
        if (difficulty == Difficulty.NORMAL) {
            return normalAchievePoints;
        }

        int[] scaledAchievePoints = new int[NUM_ACHIEVEMENTS];
        for (int i = 0; i < normalAchievePoints.length; i++) {
            switch(difficulty) {
                case EASY:
                    scaledAchievePoints[i] = (int) (normalAchievePoints[i] * EASY_SCALE_FACTOR);
                    break;
                case HARD:
                    scaledAchievePoints[i] = (int) (normalAchievePoints[i] * HARD_SCALE_FACTOR);
                    break;
            }
        }

        return scaledAchievePoints;
    }

    private void setAchievementLevel(int achievementLevel) {
        this.achievementLevel = achievementLevel;
    }

    private int calculateAchievementLevel(int[] potentialAchievePoints, int theScore) {
        int achievementLevel = 0;

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

    public int getNextAchievementLevel() {
        // Set restriction if achievementLevel reach the last position
        if (achievementLevel < normalAchievePoints.length - 1) {
            return achievementLevel + 1;
        } else {
            return achievementLevel;
        }
    }
}
