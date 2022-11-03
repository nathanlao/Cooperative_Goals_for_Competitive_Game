package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewAchievementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_achievement);

        //getSupportActionBar().hide();

        populateAchievements();
    }

    private void populateAchievements() {
        //TODO later get actual achievement
        String[] testListAchievements = {"One", "Two", "Three", "Four",
                "Five", "Six", "Seven", "Eight"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_view_achievement,
                testListAchievements);

        ListView achieveList = findViewById(R.id.listViewAchieveCollection);
        achieveList.setAdapter(adapter);
    }
}