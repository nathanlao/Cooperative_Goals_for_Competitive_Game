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
import android.widget.Toast;

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

        setupListGameConfigs();
        populateGameConfigList();
    }

    private void setupListGameConfigs() {
        // Store gameConfigs from gameManager into gameConfigs arraylist
        gameConfigs = gameManager.getGameConfigs();
//        gameConfigs.add(new GameConfig("hello", 20, 30));
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
        getMenuInflater().inflate(R.menu.menu_game_configurations, menu);
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

    private void populateGameConfigList() {
        // Build adapter
        ArrayAdapter<GameConfig> adapter = new gameConfigListAdapter();

        // Configure the list view
        ListView list = findViewById(R.id.gameConfigListView);

        list.setAdapter(adapter);
    }

    private class gameConfigListAdapter extends ArrayAdapter<GameConfig> {
        public gameConfigListAdapter() {
            super(MainActivity.this, R.layout.game_config_item_view);
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

            // Fill the view
            TextView gameNameView = gameView.findViewById(R.id.txtGameName);
            gameNameView.setText(currentGame.getName());

            return gameView;
        }
    }
}