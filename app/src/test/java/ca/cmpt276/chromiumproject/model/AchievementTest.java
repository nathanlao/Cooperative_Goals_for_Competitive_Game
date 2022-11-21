package ca.cmpt276.chromiumproject.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

// TODO: Class level comment
class AchievementTest {

    @org.junit.jupiter.api.Test
    void testObjectCreation() {
        Achievement achievement = new Achievement(1, 1, 1, 10);
        assertEquals(0, achievement.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testWorstAchievementLevel() {
        int poorScore = 1;
        int greatScore = 81;

        // Worst Level boundary for 1 player: < 1
        // Test on 1 player, 0 score
        Achievement worstAchievement = new Achievement(1, 0, poorScore, greatScore);
        assertEquals(-1, worstAchievement.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanWorstAchievement = new Achievement(1, -1, poorScore, greatScore);
        assertEquals(-1, lessThanWorstAchievement.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanWorstAchievement = new Achievement(1, 1, poorScore, greatScore);
        assertNotEquals(-1, greaterThanWorstAchievement.getCurAchievementLevel());

        // Worst Level boundary for 1 player: < 10
        // Test on 10 players, 0 score
        Achievement multiPlayersWorstAchievement = new Achievement(10, 0, poorScore, greatScore);
        assertEquals(-1, multiPlayersWorstAchievement.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessWorstAchievement = new Achievement(10, -1, poorScore, greatScore);
        assertEquals(-1, playersLessWorstAchievement.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterWorstAchievement = new Achievement(10, 11, poorScore, greatScore);
        assertNotEquals(-1, playersGreaterWorstAchievement.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelOne() {
        int poorScore = 1;
        int greatScore = 81;

        // Level one boundary for 1 player: 1 - 10
        // Test on 1 player, 1 score
        Achievement achievementLevelOne = new Achievement(1, 1, poorScore, greatScore);
        assertEquals(0, achievementLevelOne.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelOne = new Achievement(1, 0, poorScore, greatScore);
        assertNotEquals(0, lessThanLevelOne.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelOne = new Achievement(1, 11, poorScore, greatScore);
        assertNotEquals(0, greaterThanLevelOne.getCurAchievementLevel());

        // Level one boundary for 10 players: 10 - 109
        // Test on 10 players, 10 score
        Achievement multiPlayersLevelOne = new Achievement(10, 10, poorScore, greatScore);
        assertEquals(0, multiPlayersLevelOne.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelOne = new Achievement(10, 9, poorScore, greatScore);
        assertNotEquals(0, playersLessLevelOne.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelOne = new Achievement(10, 110, poorScore, greatScore);
        assertNotEquals(0, playersGreaterLevelOne.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelTwo() {
        int poorScore = 1;
        int greatScore = 81;

        // Level Two boundary for 1 player: 11 - 20
        // Test on 1 player, 11 score
        Achievement achievementLevelTwo = new Achievement(1, 11, poorScore, greatScore);
        assertEquals(1, achievementLevelTwo.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelTwo = new Achievement(1, 10, poorScore, greatScore);
        assertNotEquals(1, lessThanLevelTwo.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelTwo = new Achievement(1, 21, poorScore, greatScore);
        assertNotEquals(1, greaterThanLevelTwo.getCurAchievementLevel());

        // Level two boundary for 10 players: 110 - 209
        // Test on 10 players, 210 score
        Achievement multiPlayersLevelTwo = new Achievement(10, 110, poorScore, greatScore);
        assertEquals(1, multiPlayersLevelTwo.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelTwo = new Achievement(10, 109, poorScore, greatScore);
        assertNotEquals(1, playersLessLevelTwo.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelTwo = new Achievement(10, 210, poorScore, greatScore);
        assertNotEquals(1, playersGreaterLevelTwo.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelThree() {
        int poorScore = 1;
        int greatScore = 81;

        // Level Three boundary for 1 player: 21 - 31
        // Test on 1 player, 21 score
        Achievement achievementLevelThree = new Achievement(1, 21, poorScore, greatScore);
        assertEquals(2, achievementLevelThree.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelThree = new Achievement(1, 20, poorScore, greatScore);
        assertNotEquals(2, lessThanLevelThree.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelThree = new Achievement(1, 32, poorScore, greatScore);
        assertNotEquals(2, greaterThanLevelThree.getCurAchievementLevel());

        // Level three boundary for 10 players: 210 - 309
        // Test on 10 players, 210 score
        Achievement multiPlayersLevelThree = new Achievement(10, 210, poorScore, greatScore);
        assertEquals(2, multiPlayersLevelThree.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelThree = new Achievement(10, 209, poorScore, greatScore);
        assertNotEquals(2, playersLessLevelThree.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelThree = new Achievement(10, 310, poorScore, greatScore);
        assertNotEquals(2, playersGreaterLevelThree.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelFour() {
        int poorScore = 1;
        int greatScore = 81;

        // Level four boundary for 1 player: 31 - 40
        // Test on 1 player, 31 score
        Achievement achievementLevelFour = new Achievement(1, 31, poorScore, greatScore);
        assertEquals(3, achievementLevelFour.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelFour = new Achievement(1, 30, poorScore, greatScore);
        assertNotEquals(3, lessThanLevelFour.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelFour = new Achievement(1, 41, poorScore, greatScore);
        assertNotEquals(3, greaterThanLevelFour.getCurAchievementLevel());

        // Level four boundary for 10 players: 310 - 409
        // Test on 10 players, 310 score
        Achievement multiPlayersLevelFour = new Achievement(10, 310, poorScore, greatScore);
        assertEquals(3, multiPlayersLevelFour.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelFour = new Achievement(10, 309, poorScore, greatScore);
        assertNotEquals(3, playersLessLevelFour.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelFour = new Achievement(10, 410, poorScore, greatScore);
        assertNotEquals(3, playersGreaterLevelFour.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelFive() {
        int poorScore = 1;
        int greatScore = 81;

        // Level five boundary for 1 player: 41 - 50
        // Test on 1 player, 41 score
        Achievement achievementLevelFive = new Achievement(1, 41, poorScore, greatScore);
        assertEquals(4, achievementLevelFive.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelFive = new Achievement(1, 40, poorScore, greatScore);
        assertNotEquals(4, lessThanLevelFive.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelFive = new Achievement(1, 51, poorScore, greatScore);
        assertNotEquals(4, greaterThanLevelFive.getCurAchievementLevel());

        // Level four boundary for 10 players: 410 - 509
        // Test on 10 players, 410 score
        Achievement multiPlayersLevelFive = new Achievement(10, 410, poorScore, greatScore);
        assertEquals(4, multiPlayersLevelFive.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelFive = new Achievement(10, 409, poorScore, greatScore);
        assertNotEquals(4, playersLessLevelFive.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelFive = new Achievement(10, 510, poorScore, greatScore);
        assertNotEquals(4, playersGreaterLevelFive.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelSix() {
        int poorScore = 1;
        int greatScore = 81;

        // Level six boundary for 1 player: 51 - 60
        // Test on 1 player, 51 score
        Achievement achievementLevelSix = new Achievement(1, 51, poorScore, greatScore);
        assertEquals(5, achievementLevelSix.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelSix = new Achievement(1, 50, poorScore, greatScore);
        assertNotEquals(5, lessThanLevelSix.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelSix = new Achievement(1, 61, poorScore, greatScore);
        assertNotEquals(5, greaterThanLevelSix.getCurAchievementLevel());

        // Level four boundary for 10 players: 510 - 609
        // Test on 10 players, 510 score
        Achievement multiPlayersLevelSix = new Achievement(10, 510, poorScore, greatScore);
        assertEquals(5, multiPlayersLevelSix.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelSix = new Achievement(10, 509, poorScore, greatScore);
        assertNotEquals(5, playersLessLevelSix.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelSix = new Achievement(10, 610, poorScore, greatScore);
        assertNotEquals(5, playersGreaterLevelSix.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelSeven() {
        int poorScore = 1;
        int greatScore = 81;

        // Level seven boundary for 1 player: 61 - 70
        // Test on 1 player, 61 score
        Achievement achievementLevelSeven = new Achievement(1, 61, poorScore, greatScore);
        assertEquals(6, achievementLevelSeven.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelSeven = new Achievement(1, 60, poorScore, greatScore);
        assertNotEquals(6, lessThanLevelSeven.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelSeven = new Achievement(1, 71, poorScore, greatScore);
        assertNotEquals(6, greaterThanLevelSeven.getCurAchievementLevel());

        // Level four boundary for 10 players: 610 - 709
        // Test on 10 players, 610 score
        Achievement multiPlayersLevelSeven = new Achievement(10, 610, poorScore, greatScore);
        assertEquals(6, multiPlayersLevelSeven.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelSeven = new Achievement(10, 609, poorScore, greatScore);
        assertNotEquals(6, playersLessLevelSeven.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelSeven = new Achievement(10, 710, poorScore, greatScore);
        assertNotEquals(6, playersGreaterLevelSeven.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testAchievementLevelEight() {
        int poorScore = 1;
        int greatScore = 81;

        // Level seven boundary for 1 player: 71 - 80
        // Test on 1 player, 71 score
        Achievement achievementLevelEight = new Achievement(1, 71, poorScore, greatScore);
        assertEquals(7, achievementLevelEight.getCurAchievementLevel());

        // Test on 1 player, score less than end boundary
        Achievement lessThanLevelEight = new Achievement(1, 70, poorScore, greatScore);
        assertNotEquals(7, lessThanLevelEight.getCurAchievementLevel());

        // Test on 1 player, score greater than end boundary
        Achievement greaterThanLevelEight = new Achievement(1, 100, poorScore, greatScore);
        assertEquals(7, greaterThanLevelEight.getCurAchievementLevel());

        // Level four boundary for 10 players: 610 - 709
        // Test on 10 players, 710 score
        Achievement multiPlayersLevelEight = new Achievement(10, 710, poorScore, greatScore);
        assertEquals(7, multiPlayersLevelEight.getCurAchievementLevel());

        // Test on 10 players, score less than end boundary
        Achievement playersLessLevelEight = new Achievement(10, 709, poorScore, greatScore);
        assertNotEquals(7, playersLessLevelEight.getCurAchievementLevel());

        // Test on 10 players, score greater than end boundary
        Achievement playersGreaterLevelEight = new Achievement(10, 1000, poorScore, greatScore);
        assertEquals(7, playersGreaterLevelEight.getCurAchievementLevel());
    }

    @org.junit.jupiter.api.Test
    void testGetStaticAchievePointsByDifficulty() {
        GameConfig gameConfig = new GameConfig("TestGame", 1, 100);

        // Test on 1 player
        // Achievement points scaled by normal level (Keeping the same boundary for original calculation)
        int[] normalScaleList = Achievement.getStaticAchievePointsByDifficulty(1, gameConfig, Difficulty.NORMAL);
        assertArrayEquals(new int[] {1, 13, 25, 37, 49, 61, 73, 85}, normalScaleList);

        // Achievement points scaled by easy level (Each achievement * 0.75)
        int[] easyScaleList = Achievement.getStaticAchievePointsByDifficulty(1, gameConfig, Difficulty.EASY);
        assertArrayEquals(new int[] {0, 9, 18, 27, 36, 45, 54, 63}, easyScaleList);

        // Achievement points scaled by hard level (Each achievement * 1.25)
        int[] hardScaleList = Achievement.getStaticAchievePointsByDifficulty(1, gameConfig, Difficulty.HARD);
        assertArrayEquals(new int[] {1, 16, 31, 46, 61, 76, 91, 106}, hardScaleList);

        // Test on 10 players
        int[] multiPLayersNormalList = Achievement.getStaticAchievePointsByDifficulty(10, gameConfig, Difficulty.NORMAL);
        assertArrayEquals(new int[] {10, 133, 256, 379, 502, 625, 748, 871}, multiPLayersNormalList);

        int[] multiPLayersEasyList = Achievement.getStaticAchievePointsByDifficulty(10, gameConfig, Difficulty.EASY);
        assertArrayEquals(new int[] {7, 99, 192, 284, 376, 468, 561, 653}, multiPLayersEasyList);

        int[] multiPLayersHardList = Achievement.getStaticAchievePointsByDifficulty(10, gameConfig, Difficulty.HARD);
        assertArrayEquals(new int[] {12, 166, 320, 473, 627, 781, 935, 1088}, multiPLayersHardList);

    }

    @Disabled("Disable test until get clarity from team: scaleAchievePointsToDifficulty() couldn't reach default case")
    @org.junit.jupiter.api.Test
    void testThrowsGetStaticAchievePointsByDifficulty() {
        GameConfig gameConfig = new GameConfig("TestGame", 1, 100);
        assertThrows(NullPointerException.class,
                () -> Achievement.getStaticAchievePointsByDifficulty(1, gameConfig, null));
    }
}