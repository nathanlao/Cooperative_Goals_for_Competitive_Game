package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

public class StatisticsActivity extends AppCompatActivity {
    private static final String GAME_CONFIG_POSITION =
            "ca.cmpt276.chromiumproject - Selected particular Game Config position";
    private static final int MAX_NUM_OF_ACHIEVEMENTS = 9;

    private int gameConfigPosition;
    private GameManager gameManager = GameManager.getInstance();
    private GameConfig gameConfigs;

    //private List<String> curAchievement;
    private String[] curAchievement;
    private List<Integer> curAchievementScoreCollections;

    private List<GameRecord> gameRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        curAchievement = getResources().getStringArray(R.array.achievement_dark_tribe);

        extractDataFromIntent();

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

    private void collectDataFromGameRecordsList() {
        int gameRecordSize = gameRecords.size();

        //TODO : Don't let user do it if gameRecord = 0, null

        int curAchievementLevel = 0;
        int curCollectedPoint = 0;
        for (int i = 0; i < gameRecordSize; i++) {
            GameRecord currentRecord = gameRecords.get(i);
            curAchievementLevel = currentRecord.getAchievementLevel() + 1;

            System.out.println("THE LEVEL IS: " + curAchievementLevel);

            curCollectedPoint =
                    curAchievementScoreCollections.get(curAchievementLevel) + 1;

            curAchievementScoreCollections.set(curAchievementLevel, curCollectedPoint);
        }
    }

    private void setUpChart() {
        //Populate list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();

        //int achieveSize = curAchievement.length;
        for (int i = 0; i < MAX_NUM_OF_ACHIEVEMENTS; i++) {
            pieEntries.add(new PieEntry(
                    curAchievementScoreCollections.get(i),
                    "Achievement #" + i));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Achievement Statistics");


        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);

        //Get chart
        PieChart chart = findViewById(R.id.chartView);
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

        gameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);
        gameRecords = gameConfigs.getGameRecords();
    }
}