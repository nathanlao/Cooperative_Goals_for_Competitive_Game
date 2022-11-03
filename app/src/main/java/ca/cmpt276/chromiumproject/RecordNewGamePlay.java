package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.chromiumproject.model.Achievement;
import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

public class RecordNewGamePlay extends AppCompatActivity {

    public static final String EXTRA_RECORD_GAME_POSITION = "Record Intent Extra - gameConfig position";

    private GameManager gameManager;
    private GameRecord gameRecord;
    private GameConfig gameConfigs;

    private int gameConfigPosition;

    private TextView numPlayers;
    private TextView combinedScore;

    public static Intent makeRecordIntent(Context context, int position) {
        Intent intent =  new Intent(context, RecordNewGamePlay.class);
        intent.putExtra(EXTRA_RECORD_GAME_POSITION, position);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new_game_play);
//        setTitle("Record New Game Play");

        gameManager = GameManager.getInstance();

        numPlayers = findViewById(R.id.numPlayersInput);
        combinedScore = findViewById(R.id.combinedScoreInput);

        extractPositionFromIntent();

    }

    private void extractPositionFromIntent() {
        Intent intent = getIntent();
        gameConfigPosition = intent.getIntExtra(EXTRA_RECORD_GAME_POSITION, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu
        getMenuInflater().inflate(R.menu.menu_record_new_game_played, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save:

                setupGameRecordInput();

                Toast.makeText(this, "Game Record Saved!", Toast.LENGTH_SHORT).show();
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupGameRecordInput() {

        int numberOfPlayersNum = 0;
        int combinedScoreNum = 0;
        gameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);

        String  numberOfPlayersStr = numPlayers.getText().toString();
        try {
            numberOfPlayersNum = Integer.parseInt(numberOfPlayersStr);
        } catch (NumberFormatException ex) {
            Log.d("Number of players: ", "NumberFormatException caught: ");
        }

        String combinedScoreStr = combinedScore.getText().toString();
        try {
            combinedScoreNum = Integer.parseInt(combinedScoreStr);
        } catch (NumberFormatException ex) {
            Log.d("Combined Score: ", "NumberFormatException caught: ");
        }

        Achievement achievement = new Achievement();
        achievement.setCurAchievement(numberOfPlayersNum, combinedScoreNum, gameConfigs);

        gameRecord = new GameRecord(numberOfPlayersNum, combinedScoreNum, gameConfigs, achievement);

        gameConfigs.addGameRecord(gameRecord);
    }
}