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

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameRecord;

public class RecordNewGamePlay extends AppCompatActivity {

    GameRecord gameRecord;
    GameConfig gameConfigs;

    TextView numPlayers;
    TextView combinedScore;

    public static Intent makeRecordIntent(Context context) {
        return new Intent(context, RecordNewGamePlay.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new_game_play);
//        setTitle("Record New Game Play");

        numPlayers = findViewById(R.id.numPlayersInput);
        combinedScore = findViewById(R.id.combinedScoreInput);

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
                //GameRecord gameRecord = new GameRecord();

                setupGameRecordInput();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupGameRecordInput() {

        int numberOfPlayersNum = 0;
        int combinedScoreNum = 0;

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

        gameRecord = new GameRecord(numberOfPlayersNum, combinedScoreNum, gameConfigs);
    }
}