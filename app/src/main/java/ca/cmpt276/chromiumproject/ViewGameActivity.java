package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

public class ViewGameActivity extends AppCompatActivity {

    public static final String POSITION = "POSITION";
    private int position;

    private GameManager gameManager;

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

    private ArrayAdapter<String> populateGamesRecordListView() {
        ///create list of items
//        GameConfig gameConfig = new GameConfig();
//        List<String> gamesPlayed = gameConfig.getGameRecordStrings();
        //adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.games_played); //gamesPlayed);
        ListView gamesPlayedList = (ListView) findViewById(R.id.gamesPlayedListView);
        gamesPlayedList.setAdapter(adapter);
        return adapter;
    }
}