package ca.cmpt276.chromiumproject;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ca.cmpt276.chromiumproject.model.Achievement;
import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

/**EarnedAchievementActivity is displayed after a user saves a new game record and earns an achievement level. A congratulatory message appears alongside a
 * sound effect, image, animation, and the achievement level earned.
 * Background photo from https://unsplash.com/photos/xplrF8WMitE, firework animation from cupids@aphrodite.global
 * Sound from https://www.youtube.com/watch?v=h3QMnHw0fi8
 */
public class EarnedAchievementActivity extends AppCompatActivity {

    public static final int ANIMATION_DURATION = 6000;
    public static final int FIREWORKS1_X_TRANSLATION = 600;
    public static final int FIREWORKS1_Y_TRANSLATION = -600;
    public static final int FIREWORKS2_X_TRANSLATION = -600;
    public static final int FIREWORKS2_Y_TRANSLATION = -600;
    public static final String GAME_CONFIG_POSITION = "game config position";
    public static final String COMBINED_SCORE = "combined score";
    public static final int DEFAULT_VALUE = 0;
    private static int SPLASH_TIME = 10*1000;

    private TextView earnedAchievementTxt;
    private TextView earnedNextAchievementTxt;
    private String[] achievementCollections;

    private GameManager gameManager;
    private GameRecord gameRecord;
    private GameConfig gameConfig;
    MediaPlayer cheeringAudio;
    private int gameConfigPosition;

    private int combinedScore;
    ImageView fireworks1;
    ImageView fireworks2;

    public static Intent makeEarnedAchievementIntent(Context c, int gameConfigPosition, int combinedScore) {
        Intent intent = new Intent(c, EarnedAchievementActivity.class);
        intent.putExtra(GAME_CONFIG_POSITION, gameConfigPosition);
        intent.putExtra(COMBINED_SCORE, combinedScore);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();
        gameConfigPosition = intent.getIntExtra(GAME_CONFIG_POSITION, DEFAULT_VALUE);
        combinedScore = intent.getIntExtra(COMBINED_SCORE, DEFAULT_VALUE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earned_achievement);

        gameManager = GameManager.getInstance();

        setTitle(getString(R.string.earned_achievement));

        extractDataFromIntent();
        setCelebrationMessageText(gameConfigPosition);
        setNextAchievementLevelText(gameConfigPosition);

        fireworks1 = findViewById(R.id.fireworksAnimation);
        fireworks1.bringToFront();
        fireworks2 = findViewById(R.id.fireworksAnimation2);
        fireworks2.bringToFront();

        runAnimation(fireworks1, FIREWORKS1_X_TRANSLATION, FIREWORKS1_Y_TRANSLATION);
        runAnimation(fireworks2, FIREWORKS2_X_TRANSLATION, FIREWORKS2_Y_TRANSLATION);
        cheeringAudio = MediaPlayer.create(EarnedAchievementActivity.this, R.raw.celebration_audio);
        cheeringAudio.start();


        setUpEndBtn();
        setUpReplayBtn();

    }

    private void setUpReplayBtn() {
        Button replayBtn = (Button) findViewById(R.id.replayBtn);
        replayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runAnimation(fireworks1, FIREWORKS1_X_TRANSLATION, FIREWORKS1_Y_TRANSLATION);
                runAnimation(fireworks2, FIREWORKS2_X_TRANSLATION, FIREWORKS2_Y_TRANSLATION);
                cheeringAudio = MediaPlayer.create(EarnedAchievementActivity.this, R.raw.celebration_audio);
                cheeringAudio.start();
            }
        });
    }


    private void setUpEndBtn() {
        Button endBtn = (Button)findViewById(R.id.skipBtn);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cheeringAudio.stop();
                finish();
            }
        });
    }

    private void setCelebrationMessageText(int gameConfigPosition) {
       earnedAchievementTxt = findViewById(R.id.earnedAchievementTxt);
       earnedAchievementTxt.setText(getResources().getString(R.string.achievement_level_congratulations_message, getAchievementLevel(gameConfigPosition)));
    }

    private void setNextAchievementLevelText(int gameConfigPosition) {
        earnedNextAchievementTxt = findViewById(R.id.earnedNextAchievementTxt);

        int achievementLevel = gameRecord.getNextAchievementLevel();
        if (achievementLevel == Achievement.SPECIAL_OVER_ACHIEVE) {
            earnedNextAchievementTxt.setText(R.string.earned_highest_achievement_message);
        } else {
            earnedNextAchievementTxt.setText(getResources().getString(R.string.next_achievement_level_message,
                    getNextAchievementLevel(gameConfigPosition), getPointsToNextLevel(gameConfigPosition)));
         }
    }

    private void getCurrentGameRecord(int gameConfigPosition) {
        // Retrieve current gameConfig
        gameConfig = gameManager.getGameConfigByIndex(gameConfigPosition);
        // GameRecord position would be the last position in list
        int gameRecordPosition = gameConfig.getNumGameRecords();
        gameRecord = gameConfig.getGameRecords().get(gameRecordPosition - 1);
    }

    private String getAchievementLevel(int gameConfigPosition) {
        String theme = AchievementSettingsActivity.getTheme(EarnedAchievementActivity.this);
        String[] achievementNames = getAchievementNames(theme);

        getCurrentGameRecord(gameConfigPosition);
        int achievementLevel = gameRecord.getAchievementLevel();

        return getAchievementTitle(theme, achievementNames, achievementLevel);
    }

    private String getNextAchievementLevel(int gameConfigPosition) {
        String theme = AchievementSettingsActivity.getTheme(EarnedAchievementActivity.this);
        String[] achievementNames = getAchievementNames(theme);

        getCurrentGameRecord(gameConfigPosition);
        int nextAchievementLevel = gameRecord.getNextAchievementLevel();

        return getAchievementTitle(theme, achievementNames, nextAchievementLevel);
    }

    private String getPointsToNextLevel(int gameConfigPosition) {
        getCurrentGameRecord(gameConfigPosition);

        int nextAchievementPoints = gameRecord.getNextLevelPoints();

        int pointsToNextLevel = nextAchievementPoints - combinedScore;
        return String.valueOf(pointsToNextLevel);
    }

    @Nullable
    private String getAchievementTitle(String theme, String[] achievementNames, int nextAchievementLevel) {
        String achievementTitle = null;
        // check if Special Worst Achievement
        if (nextAchievementLevel == Achievement.SPECIAL_WORST_ACHIEVE) {

            // set themes to achievementTitle
            String[] themeOptions = getResources().getStringArray(R.array.theme_names);

            if (Objects.equals(theme, themeOptions[0])) {
                achievementTitle = getString(R.string.special_achievement);
            } else if (Objects.equals(theme, themeOptions[1])) {
                achievementTitle = getString(R.string.enchanted_forest_special_achievement);
            } else if (Objects.equals(theme, themeOptions[2])) {
                achievementTitle = getString(R.string.dark_tribe_special_achievement);
            }
        } else {
            achievementTitle = achievementNames[nextAchievementLevel];
        }
        return achievementTitle;
    }


    private String[] getAchievementNames(String theme) {
        switch (theme) {
            case "Adventurer":
                achievementCollections = getResources().getStringArray(R.array.achievement_names);
                return achievementCollections;

            case "Enchanted Forest":
                achievementCollections = getResources().getStringArray(R.array.enchanted_forest_achievement_names);
                return achievementCollections;

            case "Dark Tribe":
                achievementCollections = getResources().getStringArray(R.array.achievement_dark_tribe);
                return achievementCollections;
        }
        return achievementCollections;
    }

    private void runAnimation(ImageView fireworks, int xTranslation, int yTranslation) {
        //animation tutorial: https://stackoverflow.com/questions/12983681/translateanimation-on-imageview-android
        //and https://stackoverflow.com/questions/20608073/how-to-rotate-imageview-from-its-centre-position


        int xPosition = 0;
        int yPosition = 0;

        AnimationSet animationSet = new AnimationSet(false);

        //translation animation
        Animation translateAnimation = new TranslateAnimation(xPosition, xPosition+ xTranslation, yPosition, yPosition+ yTranslation);
        translateAnimation.setDuration(ANIMATION_DURATION);
        translateAnimation.setFillAfter(false);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationSet.addAnimation(translateAnimation);


        fireworks.startAnimation(animationSet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_earned_achievement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {

            case R.id.action_achievement_settings:
                Intent i = AchievementSettingsActivity.makeAchievementSettingsIntent(EarnedAchievementActivity.this);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onResume() {
        super.onResume();
        setCelebrationMessageText(gameConfigPosition);
        setNextAchievementLevelText(gameConfigPosition);
    }
}