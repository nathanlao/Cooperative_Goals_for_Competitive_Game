package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.cmpt276.chromiumproject.model.Achievement;
import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

/**ViewGameConfig Activity shows users details about a game config (the game config they chose in the main activity screen).
 * It shows users buttons to Edit the game config or record a new game played, both of which open a different activity.
 * A list of past game records for the game config is also listed underneath the buttons.
 */

public class ViewGameConfigActivity extends AppCompatActivity {

    public static final String POSITION = "POSITION";
    private int gameConfigPosition;

    private GameManager gameManager;
    private GameConfig gameConfigs;

    private List<GameRecord> gameRecords = new ArrayList<>();
    ArrayAdapter<GameRecord> adapter;
    private String[] achievementCollections;

    private TextView noPastGamesText;

    public static Intent makeViewIntent(Context context, int position) {
        Intent intent = new Intent(context, ViewGameConfigActivity.class);
        intent.putExtra(POSITION, position);
        return intent;
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        gameConfigPosition = intent.getIntExtra(POSITION, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);
        setUpBackButton();

        //checking if there are past games played
        noPastGamesText = findViewById(R.id.pastGamesEmptyStateTxt);
        gameManager = GameManager.getInstance();
        updateTitle(); // updates AppBar title to be the GameConfig's name
        extractDataFromIntent();

        // setup buttons
        setUpEditConfig();
        setUpRecordNewGame();
        setUpDeleteConfig();
        setUpViewAchievement();

        // populate list of game records
        setupGamesRecordList();
        populateGamesRecordListView();

        // Edit the past games
        pastGamesClickCallBack();

        checkNoPastGames();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupGamesRecordList();
        populateGamesRecordListView();
        updateTitle();
        checkNoPastGames();
    }

    private void setUpBackButton() {
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void pastGamesClickCallBack() {
        ListView pastGamesList = findViewById(R.id.gamesPlayedListView);
        pastGamesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent pastGameIntent = RecordNewGamePlayActivity.makePastGameIntent(this, gameConfigPosition, position);
            startActivity(pastGameIntent);
        });
    }

    private void checkNoPastGames() {
        //show empty state text if there are no game plays recorded yet
        if (adapter.getCount() == 0) {
            noPastGamesText.setVisibility(View.VISIBLE);
        }

        else if (adapter.getCount() > 0){
            noPastGamesText.setVisibility(View.INVISIBLE);
        }
    }

    private void updateTitle() {
        //setting the title for the correct game config
        gameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);
        setTitle(gameConfigs.getName());
    }

    private void setUpViewAchievement() {
        Button achievementBtn = findViewById(R.id.achievementBtn);
        achievementBtn.setOnClickListener( v -> registerAchievementBtnClick());
    }

    private void registerAchievementBtnClick() {
        Intent intent = ViewAchievementActivity.makeIntent(this, gameConfigPosition);
        startActivity(intent);
    }

    private void populateGamesRecordListView() {
        adapter = new gameRecordsListAdapter();
        ListView list = findViewById(R.id.gamesPlayedListView);
        list.setAdapter(adapter);
    }

    private void setUpDeleteConfig() {
        Button deleteBtn = findViewById(R.id.deleteConfigBtn);
        deleteBtn.setOnClickListener( v -> registerDeleteBtnClick());
    }

    private void registerDeleteBtnClick() {
        GameConfig targetConfig = gameManager.getGameConfigByIndex(gameConfigPosition);
        String deleteMessage = getString(R.string.delete_msg, targetConfig.getName());
        Toast.makeText(this, deleteMessage, Toast.LENGTH_SHORT).show();

        gameManager.deleteGameConfig(targetConfig);
        // save updated gameConfigs list to SharedPrefs
        MainActivity.saveGameConfigs(this, gameManager);
        finish();
    }

    private void setUpEditConfig() {
        Button editBtn = findViewById(R.id.editConfigBtn);
        editBtn.setOnClickListener(view -> {
                Intent editIntent = AddOrEditGameConfigActivity.makeEditIntent(ViewGameConfigActivity.this, gameConfigPosition);
                startActivity(editIntent);
                });
    }

    private void setUpRecordNewGame() {
        Button recordBtn = findViewById(R.id.recordGameBtn);
        recordBtn.setOnClickListener(view -> {
            Intent recordIntent = RecordNewGamePlayActivity.makeRecordIntent(ViewGameConfigActivity.this, gameConfigPosition);
            startActivity(recordIntent);
        });
    }

    private void setupGamesRecordList() {
        // Get current game configuration and retrieve its associated game records
        gameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);
        gameRecords = gameConfigs.getGameRecords();
    }

    private class gameRecordsListAdapter extends ArrayAdapter<GameRecord> {
        public gameRecordsListAdapter() {
            super(ViewGameConfigActivity.this, R.layout.games_played_item_view, gameRecords);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
           View gamePlayedView = convertView;
           if (gamePlayedView == null) {
               gamePlayedView = getLayoutInflater().inflate(R.layout.games_played_item_view, parent, false);
           }

           GameRecord currentRecord = gameRecords.get(position);
           String theme = AchievementSettingsActivity.getTheme(ViewGameConfigActivity.this);
           String[] achievementNames = getAchievementNames(theme);

            // String msg with associated retrieved values
            String numOfPlayersMsg = getString(R.string.number_players_view);
            numOfPlayersMsg += " " + currentRecord.getNumPlayers();

            String combinedScoreMsg = getString(R.string.combined_score_view);
            combinedScoreMsg += " " + currentRecord.getCombinedScore();

            String achievementMsg = getString(R.string.achievement_level_view);
            int achievementLevel = currentRecord.getAchievementLevel();
            String achievementTitle = null;
            // check if Special Worst Achievement
            if (achievementLevel == Achievement.SPECIAL_WORST_ACHIEVE) {
                String[] themeOptions = getResources().getStringArray(R.array.theme_names);

                if (Objects.equals(theme, themeOptions[0])) {
                    achievementTitle = getString(R.string.special_achievement);
                } else if (Objects.equals(theme, themeOptions[1])) {
                    achievementTitle = getString(R.string.enchanted_forest_special_achievement);
                } else if (Objects.equals(theme, themeOptions[2])) {
                    achievementTitle = getString(R.string.dark_tribe_special_achievement);
                }
            } else {
                achievementTitle = achievementNames[currentRecord.getAchievementLevel()];
            }
            achievementMsg += " " + achievementTitle;

            String difficultyMsg = currentRecord.getDifficulty().name();

            // Fill the view creationTime, number players, combined score, achievement level and difficulty
            TextView creationTimeView = gamePlayedView.findViewById(R.id.txtGameCreationTime);
            creationTimeView.setText(currentRecord.getCreationTimeString());

            TextView numberOfPlayersView = gamePlayedView.findViewById(R.id.txtNumOfPlayers);
            numberOfPlayersView.setText(numOfPlayersMsg);

            TextView combinedScoreView = gamePlayedView.findViewById(R.id.txtCombinedScore);
            combinedScoreView.setText(combinedScoreMsg);

            TextView achievementView = gamePlayedView.findViewById(R.id.txtAchievementLevel);
            achievementView.setText(achievementMsg);

            TextView difficultyView = gamePlayedView.findViewById(R.id.txtDifficultyLevel);
            difficultyView.setText(difficultyMsg);

           return gamePlayedView;
        }
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
}