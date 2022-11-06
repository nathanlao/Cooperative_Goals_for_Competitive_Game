package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
    private static final String ACHIEVEMENT_COLLECTION_LIST =
            "ca.cmpt276.chromiumproject - List of collections of available Achievements";
    private static final String POTENTIAL_SCORE_LIST =
            "ca.cmpt276.chromiumproject - List of potential achievable scores";
    private static final String GAME_CONFIG_POSITION =
            "ca.cmpt276.chromiumproject - Selected particular Game Config position";

    private int gameConfigPosition;
    private GameManager gameManager = GameManager.getInstance();
    private GameConfig gameConfigs;

    private String[] achievementCollections = Achievement.getAchievementCollection();
    private List<String> actualAchievementList;

    private int[] potentialScoreCollections = {};
    private List<Integer> actualScoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_achievement);

        getSupportActionBar().hide();
        TextView enterTextNum = findViewById(R.id.textViewEnterMsg);
        enterTextNum.setText(R.string.num_player_text);

        extractDataFromIntent();
        setUpTextMonitor();
        //includeSpecialAchievement();

        //populateAchievements();
    }

    private void includeSpecialAchievement() {
        actualAchievementList = new ArrayList<String>(Arrays.asList(achievementCollections));
        actualAchievementList.add(0, getString(R.string.special_achievement));
        actualScoreList =
                new ArrayList<Integer>(Arrays.stream(potentialScoreCollections).boxed().collect(Collectors.toList()));
        actualScoreList.add(0, potentialScoreCollections[0]);
    }

    /*public static Intent makeIntent (Context context, String[] achieveList, int[] scoreList) {
        Intent intent = new Intent(context, ViewAchievementActivity.class);

        intent.putExtra(ACHIEVEMENT_COLLECTION_LIST, achieveList);
        intent.putExtra(POTENTIAL_SCORE_LIST, scoreList);

        return intent;
    }*/
    public static Intent makeIntent (Context context, int gameConfigPos) {
        Intent intent = new Intent(context, ViewAchievementActivity.class);

        intent.putExtra(GAME_CONFIG_POSITION, gameConfigPos);

        return intent;
    }
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        gameConfigPosition = intent.getIntExtra(GAME_CONFIG_POSITION, 0);

        //TODO----ReEnable Later after testing
        gameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);
    }
    private void setUpTextMonitor() {
        EditText numPlayerText = findViewById(R.id.editTextNumberPlayer);
        String textBoxSTring = "";
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
        EditText numPlayerText = findViewById(R.id.editTextNumberPlayer);
        String textBoxString = numPlayerText.getText().toString();
        int textBoxIntNumPlayer = 0;
        //checkEmpty
        if (TextUtils.isEmpty(textBoxString)) {
            achieveList.setAdapter(null);
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
                populateAchievements();
            }
        }

        /*textBoxIntNumPlayer = Integer.parseInt(textBoxString);

        potentialScoreCollections = Achievement.getStaticPotentialAchievePoint(textBoxIntNumPlayer, gameConfigs);

        includeSpecialAchievement();
        populateAchievements();*/

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
                scoreText.setText("" + "<" + curAchScore);

                reqText.setText(getString(R.string.display_earned_by_score));
                achieveText.setText(getString(R.string.display_special_achievement, curAchieve));
            }
            if (position != 0) {
                int curAchScore = actualScoreList.get(position);
                TextView scoreText = itemView.findViewById(R.id.item_achieveScore);
                scoreText.setText("" + curAchScore);
            }

            return itemView;
        }
    }
}