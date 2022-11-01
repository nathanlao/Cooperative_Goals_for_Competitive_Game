package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameRecord;

public class ViewGameActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        setUpEditConfig();
    }

    private void setUpEditConfig() {
        Button editBtn = (Button) findViewById(R.id.editConfigBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send edit intent to edit screen
                Intent editIntent = new Intent(ViewGameActivity.this, MainActivity.class);
                startActivity(editIntent);
            }
        });
    }

    private void setUpRecordNewGame() {
        Button recordBtn = (Button) findViewById(R.id.recordGameBtn);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send edit intent to record game screen
                Intent recordGameIntent = new Intent(ViewGameActivity.this, RecordNewGamePlay.class);
                startActivity(recordGameIntent);
            }
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