package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;
    private List<GameConfig> gameConfigs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get one instance that GameManager produced
        gameManager = GameManager.getInstance();

        // Setup and display game configuration list
        setupListGameConfigs();
        populateGameConfigList();

        // Click on one game config to edit
        gameConfigCLickCallBack();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupListGameConfigs();
        populateGameConfigList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with ADD icon
        getMenuInflater().inflate(R.menu.menu_add_game_configurations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add_game_config:
                // Click ADD icon directs to ADD or EDIT game config
                Intent i = AddOrEditGameConfigActivity.makeAddIntent(MainActivity.this);
                startActivity(i);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupListGameConfigs() {
        // Store gameConfigs from gameManager into gameConfigs arraylist
        gameConfigs = gameManager.getGameConfigs();
    }

    private void populateGameConfigList() {
        // Build adapter
        ArrayAdapter<GameConfig> adapter = new gameConfigListAdapter();

        // Configure the list view
        ListView list = findViewById(R.id.gameConfigListView);

        list.setAdapter(adapter);
    }

    private void gameConfigCLickCallBack() {
        ListView list = findViewById(R.id.gameConfigListView);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {

            // Send position to AddOrEditGameConfigActivity
            Intent i = AddOrEditGameConfigActivity.makeEditIntent(MainActivity.this, position);
            startActivity(i);
        });
    }

    private class gameConfigListAdapter extends ArrayAdapter<GameConfig> {
        public gameConfigListAdapter() {
            super(MainActivity.this, R.layout.game_config_item_view, gameConfigs);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Reuse game view (inflate one if null)
            View gameView = convertView;
            if (gameView == null) {
                gameView = getLayoutInflater().inflate(R.layout.game_config_item_view, parent, false);
            }

            GameConfig currentGame = gameConfigs.get(position);

            // TODO: Probably fill with game image later

            // Fill the view with gameName, poor score and great score
            TextView gameNameView = gameView.findViewById(R.id.txtGameName);
            gameNameView.setText(currentGame.getName());

            TextView poorScoreView = gameView.findViewById(R.id.txtPoorScore);
            poorScoreView.setText("Poor Score: " + currentGame.getPoorScore());

            TextView greatScoreView = gameView.findViewById(R.id.txtGreatScore);
            greatScoreView.setText("Great Score: " + currentGame.getGreatScore());


            return gameView;
        }
    }
}