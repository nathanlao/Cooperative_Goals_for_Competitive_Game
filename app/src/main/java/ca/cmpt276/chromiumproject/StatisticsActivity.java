package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    //private List<String> curAchievement;
    private String[] curAchievement;
    private List<Integer> curAchievementScoreCollections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        curAchievement = getResources().getStringArray(R.array.achievement_dark_tribe);

        setUpChart();
    }

    private void setUpChart() {
        //Populate list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();

        int chartSize = curAchievement.length;
        for (int i = 0; i < chartSize; i++) {
            pieEntries.add(new PieEntry(curAchievementScoreCollections.get(i), curAchievement[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Achievement Statistics");

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);

        //Get chart
        PieChart chart = findViewById(R.id.chartView);
        chart.setData(data);
        chart.animateY(3000);
        chart.invalidate();
    }
}