package ca.cmpt276.chromiumproject.model;

public class Achievement {
    private static final int NUM_ACHIEVEMENTS = 8;

    private String curAchievement;
    private String[] achievementCollection = {"Achievement #1", "Achievement #2",
            "Achievement #3", "Achievement #4", "Achievement #5", "Achievement #6",
            "Achievement #7", "Achievement #8"};
    private int[] potentialAchievePoints = {};

    //Singleton connection to GameConfig
    private GameConfig curGameConfig = GameConfig.getInstance();

    public Achievement() {
        this.curAchievement = "Initial Empty";
    }

    public void setCurAchievement(int playerCount, int theScore) {
        int lowestAchieve = playerCount * curGameConfig.getPoorScore();
        int highestAchieve = playerCount * curGameConfig.getGreatScore();

        int middleGround = highestAchieve - lowestAchieve;

        int partitionNum = middleGround / NUM_ACHIEVEMENTS;

        //Set initial location for loop
        int partitionMultiplier = 0;
        int curAchieveLocation = 0;
        int curAchieveEndBoundary = 0;
        for (int i = 0; i < NUM_ACHIEVEMENTS; i++) {


            partitionMultiplier = i * partitionNum;

            //Set the Boundary for calculation purpose
            curAchieveLocation = lowestAchieve + partitionMultiplier;
            curAchieveEndBoundary = curAchieveLocation + partitionMultiplier;

            //check on first loop, for Special worst achievement
            if (i == 0) {
                if (theScore < curAchieveLocation) {
                    curAchievement = "Achievement #0 Failure";
                }
            }

            //If score is in the Boundary, choose from achievement Collection
            if (theScore >= curAchieveLocation &&
            theScore < curAchieveEndBoundary) {
                chooseFromAchieveCollection(i);
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

    public void setPotentialAchievePoint(int playerCount) {
        int lowestAchieve = playerCount * curGameConfig.getPoorScore();
        int highestAchieve = playerCount * curGameConfig.getGreatScore();

        int middleGround = highestAchieve - lowestAchieve;

        int partitionNum = middleGround / NUM_ACHIEVEMENTS;

        //Set initial location for loop
        int partitionMultiplier = 0;
        int curAchieveLocation = 0;
        int curAchieveEndBoundary = 0;

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

}
