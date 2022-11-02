package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewAchievementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_achievement);

        getSupportActionBar().hide();

        populateAchievements();
    }

    private void populateAchievements() {

    }
}