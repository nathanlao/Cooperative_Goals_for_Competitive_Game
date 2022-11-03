package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewAchievementActivity extends AppCompatActivity {
    private static final String ACHIEVEMENT_COLLECTION_LIST =
            "ca.cmpt276.chromiumproject - List of collections of available Achievements";
    private static final String POTENTIAL_SCORE_LIST =
            "ca.cmpt276.chromiumproject - List of potential achievable scores";

    private String[] achievementCollections;
    private int[] potentialScoreCollections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_achievement);

        getSupportActionBar().hide();

        extractDataFromIntent();

        populateAchievements();
    }

    public static Intent makeIntent (Context context, String[] achieveList, int[] scoreList) {
        Intent intent = new Intent(context, ViewAchievementActivity.class);

        intent.putExtra(ACHIEVEMENT_COLLECTION_LIST, achieveList);
        intent.putExtra(POTENTIAL_SCORE_LIST, scoreList);

        return intent;
    }
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        //String[] tempStrArr = {""};
        //int[] tempIntArr = {0};

        achievementCollections = intent.getStringArrayExtra(ACHIEVEMENT_COLLECTION_LIST);
        potentialScoreCollections = intent.getIntArrayExtra(POTENTIAL_SCORE_LIST);
    }

    private void populateAchievements() {
        ArrayAdapter<String> adapterAchieve = new MyListAdapter();

        ListView achieveList = findViewById(R.id.listViewAchieveCollection);
        achieveList.setAdapter(adapterAchieve);
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(ViewAchievementActivity.this, R.layout.achievement_view, achievementCollections);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.achievement_view, parent, false);
            }

            String curAchieve = achievementCollections[position];

            TextView achieveText = itemView.findViewById(R.id.item_achievement);
            achieveText.setText(curAchieve);


            int curAchScore = potentialScoreCollections[position];
            TextView scoreText = itemView.findViewById(R.id.item_achieveScore);
            scoreText.setText("" + curAchScore);

            return itemView;
        }
    }
}