package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

/** StatisticsActivity retrieves existing individual achievement data from game records that
 * reside inside a single game config. It generates pie chart showing users
 * how much of each level of achievement is earned in each slice of pie.
 */
public class StatisticsActivity extends AppCompatActivity {
    private static final String GAME_CONFIG_POSITION =
            "ca.cmpt276.chromiumproject - Selected particular Game Config position";
    private static final int MAX_NUM_OF_ACHIEVEMENTS = 9;

    private int gameConfigPosition;
    private GameManager gameManager = GameManager.getInstance();
    private GameConfig gameConfig;

    private List<Integer> curAchievementScoreCollections;

    private List<GameRecord> gameRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        setUpBackButton();

        extractDataFromIntent();
        checkPotentialError();

        initializeScoreCollection();
        collectDataFromGameRecordsList();
        setUpChart();
    }

    private void initializeScoreCollection() {
        curAchievementScoreCollections = new ArrayList<>();
        for (int i = 0; i < MAX_NUM_OF_ACHIEVEMENTS; i++) {
            curAchievementScoreCollections.add(0);
        }
    }

    private void checkPotentialError() {
        if (gameRecords.size() == 0) {
            Toast.makeText(this, getString(R.string.add_game_warn), Toast.LENGTH_LONG).show();
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        }
    }

    private void collectDataFromGameRecordsList() {
        int gameRecordSize = gameRecords.size();

        int curAchievementLevel = 0;
        int curCollectedPoint = 0;
        for (int i = 0; i < gameRecordSize; i++) {
            GameRecord currentRecord = gameRecords.get(i);
            curAchievementLevel = currentRecord.getAchievementLevel() + 1;

            curCollectedPoint =
                    curAchievementScoreCollections.get(curAchievementLevel) + 1;

            curAchievementScoreCollections.set(curAchievementLevel, curCollectedPoint);
        }
    }

    private void setUpChart() {
        //Populate list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < MAX_NUM_OF_ACHIEVEMENTS; i++) {
            if (curAchievementScoreCollections.get(i) > 0) {
                pieEntries.add(new PieEntry(
                        curAchievementScoreCollections.get(i),
                        getString(R.string.word_achieve) +
                                " #" + i));
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, getString(R.string.word_achieves));


        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTextSize(13f);

        PieData data = new PieData(dataSet);

        //Get chart
        PieChart chart = findViewById(R.id.chartView);

        //Disable description label and legends
        chart.getLegend().setEnabled(false);
        chart.setDescription(null);

        chart.setData(data);
        chart.animateY(1000);

        chart.invalidate();
    }

    public static Intent makeIntent (Context context, int gameConfigPos) {
        Intent intent = new Intent(context, StatisticsActivity.class);

        intent.putExtra(GAME_CONFIG_POSITION, gameConfigPos);

        return intent;
    }
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        gameConfigPosition = intent.getIntExtra(GAME_CONFIG_POSITION, 0);

        gameConfig = gameManager.getGameConfigByIndex(gameConfigPosition);
        gameRecords = gameConfig.getGameRecords();
    }

    private void setUpBackButton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}