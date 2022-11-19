package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
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
import java.util.stream.Collectors;

import ca.cmpt276.chromiumproject.model.Achievement;
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

    private EditText numPlayerText;
    private Button normalBtn;
    private Button easyBtn;
    private Button hardBtn;

    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_achievement);

        //achievementCollections = getResources().getStringArray(R.array.achievement_names);
        setTheme();
        achievementCollections = getAchievementNames(theme);
        TextView enterTextNum = findViewById(R.id.textViewEnterMsg);
        enterTextNum.setText(R.string.num_player_text);

        numPlayerText = findViewById(R.id.editTextNumberPlayer);
        setUpButtonField();

        setUpBackButton();

        extractDataFromIntent();
        setUpInitialButtonBehaviour();
        setUpTextMonitor();

    }

    private String[] getAchievementNames(String theme) {
        String[] themeOptions = getResources().getStringArray(R.array.theme_names);
        switch (theme) {
            case "Adventurer":
                achievementCollections = getResources().getStringArray(R.array.achievement_names);
                return achievementCollections;

            case "Enchanted Forest":
                achievementCollections = getResources().getStringArray(R.array.enchanted_forest_achievement_names);
                return achievementCollections;

            case "Dark Fantasy":
                //TODO: access the right themes
                achievementCollections = getResources().getStringArray(R.array.achievement_names);
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

    private void setUpButtonField() {
        normalBtn = findViewById(R.id.btnSelectNormal);
        easyBtn = findViewById(R.id.btnSelectEasy);
        hardBtn = findViewById(R.id.btnSelectHard);
    }

    private void setUpInitialButtonBehaviour() {
        // Initially click on button display toast message to notify user to enter playerCount
        normalBtn.setOnClickListener(v -> Toast.makeText(ViewAchievementActivity.this, R.string.toast_enter_number_of_players, Toast.LENGTH_SHORT).show());
        easyBtn.setOnClickListener(v -> Toast.makeText(ViewAchievementActivity.this, R.string.toast_enter_number_of_players, Toast.LENGTH_SHORT).show());
        hardBtn.setOnClickListener(v -> Toast.makeText(ViewAchievementActivity.this, R.string.toast_enter_number_of_players, Toast.LENGTH_SHORT).show());
    }

    private void setUpBackButton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void includeSpecialAchievement() {
        achievementCollections = getAchievementNames(theme);
        actualAchievementList = new ArrayList<String>(Arrays.asList(achievementCollections));
        switch (theme) {
            case "Adventurer":
                actualAchievementList.add(0, getString(R.string.special_achievement));

            case "Enchanted Forest":
                actualAchievementList.add(0, getString(R.string.enchanted_forest_special_achievement));

            case "Dark Fantasy":
                //TODO: access the right themes
                int one;
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

    private void setUpTextMonitor() {
        achievementCollections = getAchievementNames(theme);
        numPlayerText = findViewById(R.id.editTextNumberPlayer);
        numPlayerText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //calibrateNewAchievement();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //calibrateNewAchievement();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                calibrateNewAchievement();
            }
        });
    }

    private void calibrateNewAchievement() {

        ListView achieveList = findViewById(R.id.listViewAchieveCollection);
        String textBoxString = numPlayerText.getText().toString();
        int textBoxIntNumPlayer = 0;

        //checkEmpty
        if (TextUtils.isEmpty(textBoxString)) {
            achieveList.setAdapter(null);

            // Reset button color and clear the list to avoid mistaken display
            resetDifficultyButtonColor(normalBtn, easyBtn, hardBtn);
            actualAchievementList.clear();
        }
        if (!TextUtils.isEmpty(textBoxString)) {
            textBoxIntNumPlayer = Integer.parseInt(textBoxString);
            //case of 0
            if (textBoxIntNumPlayer == 0) {
                achieveList.setAdapter(null);
                Toast.makeText(this, R.string.zero_player_msg, Toast.LENGTH_SHORT).show();
            }
            if (textBoxIntNumPlayer > 0) {
                potentialScoreCollections = Achievement.getStaticPotentialAchievePoint(textBoxIntNumPlayer, gameConfigs);

                includeSpecialAchievement();

                // TODO: Comment out populateAchievements(), now have to click difficult buttons
                // populateAchievements();
                setUpDifficultyButton(normalBtn, easyBtn, hardBtn);
            }
        }
    }

    private void resetDifficultyButtonColor(Button normalBtn, Button easyBtn, Button hardBtn) {
        normalBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
        easyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
        hardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
    }

    private void setUpDifficultyButton(Button normalBtn, Button easyBtn, Button hardBtn) {
        normalBtn.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.btnSelectNormal:
                    String textBoxString = numPlayerText.getText().toString();

                    // Set button color
                    if (TextUtils.isEmpty(textBoxString)) {
                        Toast.makeText(this, R.string.toast_enter_number_of_players, Toast.LENGTH_SHORT).show();
                        normalBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                    } else {
                        normalBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                        easyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                        hardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));

                        // TODO: Testing purpose, delete later
                        Toast.makeText(ViewAchievementActivity.this, "Testing: normal", Toast.LENGTH_SHORT).show();

                        // TODO: Adapt normal level calculation into achievement listView by clicking normal button
                        populateAchievements();
                    }
                    break;
            }
        });

        easyBtn.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.btnSelectEasy:
                    String textBoxString = numPlayerText.getText().toString();
                    if (TextUtils.isEmpty(textBoxString)) {
                        Toast.makeText(this, R.string.toast_enter_number_of_players, Toast.LENGTH_SHORT).show();
                        easyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                    } else {
                        normalBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                        easyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                        hardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));

                        // TODO: Testing purpose, delete later
                        Toast.makeText(ViewAchievementActivity.this, "Testing: easy", Toast.LENGTH_SHORT).show();

                        // TODO: Adapt easy level calculation into achievement listView by clicking easy button
                        populateAchievements();
                    }
                    break;
            }
        });

        hardBtn.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.btnSelectHard:
                    String textBoxString = numPlayerText.getText().toString();
                    if (TextUtils.isEmpty(textBoxString)) {
                        Toast.makeText(this, R.string.toast_enter_number_of_players, Toast.LENGTH_SHORT).show();
                        hardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                    } else {
                        normalBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                        easyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
                        hardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

                        // TODO: Testing purpose, delete later
                        Toast.makeText(ViewAchievementActivity.this, "Testing: hard", Toast.LENGTH_SHORT).show();

                        // TODO: Adapt hard level calculation into achievement listView by clicking hard button
                        populateAchievements();
                    }

                    break;
            }
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
    }
}