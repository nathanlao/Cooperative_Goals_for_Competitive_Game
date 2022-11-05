/**
 * MainActivity class allows user to start by adding a new game config
 * Use ArrayAdapter displays the listView of GameConfig list handle by GameManager
 * Each GameConfig view displays name on top, followed by poor score and great score on bottom
 * Add button on toolbar directs users to AddOrEditGameConfigActivity by using intent
 * On start and when there are no game configurations a text will appear telling the user how to start adding games.
 */

package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;



public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;
    private List<GameConfig> gameConfigs = new ArrayList<>();

    private boolean isEmpty = true;
    private boolean firstOpenedApp = true;
    ArrayAdapter<GameConfig> adapter;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emptyText = findViewById(R.id.emptyText);

        // Get one instance that GameManager produced
        gameManager = GameManager.getInstance();

        // Load saved GameConfigs from SharedPrefs, if it exists
        if (savedGameConfigsExists()) {
            List<GameConfig> savedGameConfigs = getSavedGameConfigs();
            gameManager.setGameConfigs(savedGameConfigs);
        }

        // Setup and display game configuration list
        setupListGameConfigs();
        populateGameConfigList();

        // Click on one game config to edit
        gameConfigClickCallBack();
        firstOpenedApp = false;
        checkIsEmpty();
    }

    private void checkIsEmpty() {
        //show empty state text if empty or if it's the first time the user opens the app
        if (adapter.getCount() == 0 || firstOpenedApp) {
            isEmpty = true;
            emptyText.setVisibility(View.VISIBLE);
        }

        else if (adapter.getCount() > 0){
            isEmpty = false;
            emptyText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupListGameConfigs();
        populateGameConfigList();
        checkIsEmpty();
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

    @Override
    protected void onPause() {
        super.onPause();
        saveGameConfigs();
        // TODO: remove this log message later haha
        Log.i("TAG", "HEY app was paused WOAH!!!");
    }

    private void setupListGameConfigs() {
        // Store gameConfigs from gameManager into gameConfigs arraylist
        gameConfigs = gameManager.getGameConfigs();
    }

    private void populateGameConfigList() {
        // Build adapter
        adapter = new gameConfigListAdapter();

        // Configure the list view
        ListView list = findViewById(R.id.gameConfigListView);

        list.setAdapter(adapter);
    }

    private void gameConfigClickCallBack() {
        ListView list = findViewById(R.id.gameConfigListView);

        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent i = ViewGameActivity.makeViewIntent(MainActivity.this, position);
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

            // Extract string and concatenate with poor score/great score in GameConfig
            String poorScoreMsg = getString(R.string.string_poor_score);
            poorScoreMsg += " " + currentGame.getPoorScore();

            String greatScoreMsg = getString(R.string.string_great_score);
            greatScoreMsg += " " + currentGame.getGreatScore();

            // Fill the view with gameName, poor score and great score
            TextView gameNameView = gameView.findViewById(R.id.txtGameName);
            gameNameView.setText(currentGame.getName());

            TextView poorScoreView = gameView.findViewById(R.id.txtPoorScore);
            poorScoreView.setText(poorScoreMsg);

            TextView greatScoreView = gameView.findViewById(R.id.txtGreatScore);
            greatScoreView.setText(greatScoreMsg);

            return gameView;
        }
    }

    // GAME CONFIG SAVE SUPPORT

    public static final String PREFS_NAME = "AppPrefs";
    private static final String SAVED_CONFIGS_NAME = "Saved GameConfigs";
    private void saveGameConfigs() {
        // save current GameManager's list of GameConfigs to SharedPrefs as a Gson object
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameManager.getGameConfigs());
        editor.putString(SAVED_CONFIGS_NAME, json);
        editor.commit();
    }

    private List<GameConfig> getSavedGameConfigs() {
        // get saved list of GameConfigs from SharedPrefs, if it exists.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(SAVED_CONFIGS_NAME, null);
        Log.i("TAG", json);
        Type type = new TypeToken<ArrayList<GameConfig>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public boolean savedGameConfigsExists() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.contains(SAVED_CONFIGS_NAME);
    }

    @SuppressLint("ApplySharedPref")
    private void removeSavedGameConfigs() {
        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(SAVED_CONFIGS_NAME);
        editor.commit();
    }

}