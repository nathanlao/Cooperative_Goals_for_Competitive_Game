package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

public class ViewGameActivity extends AppCompatActivity {

    public static final String POSITION = "POSITION";
    private int position;

    private GameManager gameManager;
    private GameConfig gameConfigs;

    private List<GameRecord> gameRecords = new ArrayList<>();

    public static Intent makeViewIntent(Context context, int position) {
        Intent intent = new Intent(context, ViewGameActivity.class);
        intent.putExtra(POSITION, position);
        return intent;
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        position = intent.getIntExtra(POSITION, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameManager = GameManager.getInstance();

        setContentView(R.layout.activity_view_game);
        extractDataFromIntent();

        // setup buttons
        setUpEditConfig();
        setUpRecordNewGame();
        setUpDeleteConfig();

        // populate list of game records
        setupGamesRecordList();
        populateGamesRecordListView();
    }

    private void populateGamesRecordListView() {
        ArrayAdapter<GameRecord> adapter = new gameRecordsListAdapter();
        ListView list = findViewById(R.id.gamesPlayedListView);
        list.setAdapter(adapter);
    }

    private void setUpDeleteConfig() {
        Button deleteBtn = findViewById(R.id.deleteConfigBtn);
        deleteBtn.setOnClickListener( v -> registerDeleteBtnClick());
    }

    private void registerDeleteBtnClick() {
        GameConfig targetConfig = gameManager.getGameConfigByIndex(position);
        String deleteMessage = getString(R.string.delete_msg, targetConfig.getName());
        Toast.makeText(this, deleteMessage, Toast.LENGTH_SHORT).show();

        gameManager.deleteGameConfig(targetConfig);
        finish();
    }

    private void setUpEditConfig() {
        Button editBtn = findViewById(R.id.editConfigBtn);
        editBtn.setOnClickListener(view -> {
                Intent editIntent = AddOrEditGameConfigActivity.makeEditIntent(ViewGameActivity.this, position);
                startActivity(editIntent);
                });
    }

    private void setUpRecordNewGame() {
        Button recordBtn = findViewById(R.id.recordGameBtn);
        recordBtn.setOnClickListener(view -> {
            Intent recordIntent = RecordNewGamePlay.makeRecordIntent(ViewGameActivity.this);
            startActivity(recordIntent);
        });
    }

    private void setupGamesRecordList() {
        // Get current game configuration
        gameConfigs = gameManager.getGameConfigByIndex(position);
        Log.d("Current game config", gameConfigs.getName());
    }

    private class gameRecordsListAdapter extends ArrayAdapter<GameRecord> {
        public gameRecordsListAdapter() {
            super(ViewGameActivity.this, R.layout.games_played_item_view, gameRecords);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
           View gamePlayedView = convertView;
           if (gamePlayedView == null) {
               gamePlayedView = getLayoutInflater().inflate(R.layout.games_played_item_view, parent, false);
           }

           GameRecord currentRecord = gameRecords.get(position);

            // Fill the view with gameName, poor score and great score
            TextView creationTimeView = gamePlayedView.findViewById(R.id.txtGameCreationTime);
            creationTimeView.setText(currentRecord.getCreationTimeString());

            TextView numberOfPlayersView = gamePlayedView.findViewById(R.id.txtNumOfPlayers);
            numberOfPlayersView.setText(currentRecord.getNumPlayers());

            TextView combinedScoreView = gamePlayedView.findViewById(R.id.txtCombinedScore);
            combinedScoreView.setText(currentRecord.getCombinedScore());

            TextView achievementView = gamePlayedView.findViewById(R.id.txtAchievementLevel);
            achievementView.setText(currentRecord.getAchievement());

           return gamePlayedView;
        }
    }
}