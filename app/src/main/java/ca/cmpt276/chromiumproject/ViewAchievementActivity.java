package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ca.cmpt276.chromiumproject.model.Achievement;
import ca.cmpt276.chromiumproject.model.Difficulty;
import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;


/**
 * ViewAchievementActivity gets list of possible achievements you can achieve,
 * as well as potential achievable point list passed down from intent,
 * and uses these passed down lists to show up as view in the screen.
 */
public class ViewAchievementActivity extends AppCompatActivity {
    private static final String GAME_CONFIG_POSITION =
            "ca.cmpt276.chromiumproject - Selected particular Game Config position";

    private int gameConfigPosition;
    private GameManager gameManager = GameManager.getInstance();
    private GameConfig gameConfigs;

    private String[] achievementCollections;
    private List<String> actualAchievementList;

    private int[] potentialScoreCollections = {};
    private List<Integer> actualScoreList;

    private Difficulty selectedDifficulty;

    private EditText numPlayerText;
    private Button normalBtn;
    private Button easyBtn;
    private Button hardBtn;

    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_achievement);

        // get string array of Achievement titles from strings.xml
        achievementCollections = getResources().getStringArray(R.array.achievement_names);
        setTheme();
        achievementCollections = getAchievementNames(theme);
        TextView enterTextNum = findViewById(R.id.textViewEnterMsg);
        enterTextNum.setText(R.string.num_player_text);

        // set-up views
        setUpEnterNumPlayersInput();
        setUpDifficultyButtons();
        setUpBackButton();

        registerDifficultyButtonsOnClick();
        extractDataFromIntent();
        setUpNumPlayersTextWatcher();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_achievement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_achievement_settings:
                Intent i = AchievementSettingsActivity.makeAchievementSettingsIntent(ViewAchievementActivity.this);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpDifficultyButtons() {
        normalBtn = findViewById(R.id.btnSelectNormal);
        easyBtn = findViewById(R.id.btnSelectEasy);
        hardBtn = findViewById(R.id.btnSelectHard);
        resetDifficultyButtonColor();
    }

    private void setUpBackButton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void includeSpecialAchievement() {
        achievementCollections = getAchievementNames(theme);
        actualAchievementList = new ArrayList<String>(Arrays.asList(achievementCollections));
        String[] themeOptions = getResources().getStringArray(R.array.theme_names);

        if (Objects.equals(theme, themeOptions[0])) {
            actualAchievementList.add(0, getString(R.string.special_achievement));
        }
        else if (Objects.equals(theme, themeOptions[1])) {
            actualAchievementList.add(0, getString(R.string.enchanted_forest_special_achievement));
        }
        else if (Objects.equals(theme, themeOptions[2])) {
            actualAchievementList.add(0, getString(R.string.dark_tribe_special_achievement));
        }

        actualScoreList =
                new ArrayList<Integer>(Arrays.stream(potentialScoreCollections).boxed().collect(Collectors.toList()));
        actualScoreList.add(0, potentialScoreCollections[0]);
    }

    public static Intent makeIntent (Context context, int gameConfigPos) {
        Intent intent = new Intent(context, ViewAchievementActivity.class);

        intent.putExtra(GAME_CONFIG_POSITION, gameConfigPos);

        return intent;
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        gameConfigPosition = intent.getIntExtra(GAME_CONFIG_POSITION, 0);

        gameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);
    }

    private void setUpNumPlayersTextWatcher() {
        achievementCollections = getAchievementNames(theme);
        numPlayerText = findViewById(R.id.editTextNumberPlayer);
        numPlayerText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateAchievementListView();
            }
        });
    }

    private void updateAchievementListView() {
        ListView achieveList = findViewById(R.id.listViewAchieveCollection);

        // only show ListView if numPlayers is not empty and difficulty has been selected
        if (!checkDifficultyNotSelected() && !checkNumPlayersEmpty()) {
            int numPlayersInput = Integer.parseInt(numPlayerText.getText().toString());

            if (checkInvalidNumPlayers(numPlayersInput)) {
                achieveList.setAdapter(null);
            } else {
                potentialScoreCollections = Achievement.getStaticAchievePointsByDifficulty(numPlayersInput, gameConfigs, selectedDifficulty);
                includeSpecialAchievement();
                populateAchievements();
            }

        } else {
            achieveList.setAdapter(null);
        }
    }

    private boolean checkNumPlayersEmpty() {
        String numPlayersInput = numPlayerText.getText().toString();
        return TextUtils.isEmpty(numPlayersInput);
    }

    private boolean checkDifficultyNotSelected() {
        return selectedDifficulty == null;
    }

    private boolean checkInvalidNumPlayers(int numPlayers) {
        if (numPlayers <= 0) {
            Toast.makeText(this, R.string.zero_player_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void setUpEnterNumPlayersInput() {
        TextView enterTextNum = findViewById(R.id.textViewEnterMsg);
        enterTextNum.setText(R.string.num_player_text);
        numPlayerText = findViewById(R.id.editTextNumberPlayer);
    }

    private void resetDifficultyButtonColor() {
        normalBtn.setBackgroundColor(Color.GRAY);
        easyBtn.setBackgroundColor(Color.GRAY);
        hardBtn.setBackgroundColor(Color.GRAY);
    }

    private void registerDifficultyButtonsOnClick() {
        normalBtn.setOnClickListener(v -> {
            selectedDifficulty = Difficulty.NORMAL;
            resetDifficultyButtonColor();
            normalBtn.setBackgroundColor(Color.BLUE);
            updateAchievementListView();
        });

        easyBtn.setOnClickListener(v -> {
            selectedDifficulty = Difficulty.EASY;
            resetDifficultyButtonColor();
            easyBtn.setBackgroundColor(Color.GREEN);
            updateAchievementListView();
        });

        hardBtn.setOnClickListener(v -> {
            selectedDifficulty = Difficulty.HARD;
            resetDifficultyButtonColor();
            hardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            updateAchievementListView();
        });
    }

    private void populateAchievements() {
        ArrayAdapter<String> adapterAchieve = new MyListAdapter();

        ListView achieveList = findViewById(R.id.listViewAchieveCollection);
        achieveList.setAdapter(adapterAchieve);
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(ViewAchievementActivity.this, R.layout.achievement_view, actualAchievementList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.achievement_view, parent, false);
            }

            String curAchieve = actualAchievementList.get(position);

            TextView achieveText = itemView.findViewById(R.id.item_achievement);
            achieveText.setText(curAchieve);
            TextView reqText = itemView.findViewById(R.id.item_req);
            reqText.setText(getString(R.string.display_required_score));

            if (position == 0) {
                int curAchScore = actualScoreList.get(position);
                TextView scoreText = itemView.findViewById(R.id.item_achieveScore);

                String curAchScoreMsg = getString(R.string.less_than_achievement_score);
                curAchScoreMsg += "" + curAchScore;

                scoreText.setText(curAchScoreMsg);

                reqText.setText(getString(R.string.display_earned_by_score));
                achieveText.setText(getString(R.string.display_special_achievement, curAchieve));
            }
            if (position != 0) {
                int curAchScore = actualScoreList.get(position);
                TextView scoreText = itemView.findViewById(R.id.item_achieveScore);

                String curAchScoreMsg = getString(R.string.required_achievement_score);
                curAchScoreMsg += "" + curAchScore;

                scoreText.setText(curAchScoreMsg);
            }

            return itemView;
        }
    }

    public void setTheme() {
        theme = AchievementSettingsActivity.getTheme(ViewAchievementActivity.this);
    }

    protected void onResume() {
        super.onResume();
        setTheme();
        setUpEnterNumPlayersInput();
        setUpDifficultyButtons();
        setUpBackButton();

        registerDifficultyButtonsOnClick();
        extractDataFromIntent();
        setUpNumPlayersTextWatcher();
        updateAchievementListView();

    }
}