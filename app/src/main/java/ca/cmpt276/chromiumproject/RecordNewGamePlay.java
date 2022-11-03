package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ca.cmpt276.chromiumproject.model.GameRecord;

public class RecordNewGamePlay extends AppCompatActivity {

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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}