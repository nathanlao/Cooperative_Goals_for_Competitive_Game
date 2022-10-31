package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;

public class AddOrEditGameConfigActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_GAME_POSITION = "Edit Intent Extra - gameConfig position";

    private GameManager gameManager;

    private GameConfig newGameConfig = new GameConfig();
    private GameConfig editedGameConfig = new GameConfig();

    private EditText gameConfigName;
    private EditText poorScore;
    private EditText greatScore;

    private int gameConfigPosition;
    private boolean isNewGame;

    // Intent for main activity to add new game config
    public static Intent makeAddIntent(Context context) {
        return new Intent(context, AddOrEditGameConfigActivity.class);
    }

    // Intent for main activity to edit current game config
    public static Intent makeEditIntent(Context context, int position) {
            Intent intent =  new Intent(context, AddOrEditGameConfigActivity.class);
            intent.putExtra(EXTRA_EDIT_GAME_POSITION, position);
            return intent;
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_game_config);

        // Enable "up" on toolbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        gameManager = GameManager.getInstance();

        initializeEditTextFields();

        // Extract position from makeEditIntent()
        extractPositionFromIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with save icon
        getMenuInflater().inflate(R.menu.menu_save_game_configurations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save_game_config:

                String gameConfigStr = "";
                int poorScoreNum = 0;
                int greatScoreNum = 0;

                // Take in user input
                setupGameConfigFieldsInput(gameConfigStr, poorScoreNum, greatScoreNum);

                if (isNewGame) {
                    gameManager.addNewGameConfig(newGameConfig);
                } else {
                    // Replace the current game config by an edited game
                    gameManager.setGameConfigByIndex(gameConfigPosition, editedGameConfig);
                }

                Toast.makeText(this, R.string.toast_game_config_saved, Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case android.R.id.home:
                Toast.makeText(this, R.string.toast_game_config_not_saved, Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeEditTextFields() {
        gameConfigName = findViewById(R.id.editAddName);
        poorScore = findViewById(R.id.editAddPoorScore);
        greatScore = findViewById(R.id.editAddGreatScore);
    }

    private void extractPositionFromIntent() {
        Intent intent = getIntent();

        gameConfigPosition = intent.getIntExtra(EXTRA_EDIT_GAME_POSITION, -1);

        if (gameConfigPosition == -1) {
            setTitle(getString(R.string.title_add_game_configs));

            isNewGame = true;

        } else {
            setTitle(getString(R.string.title_edit_game_configs));

            isNewGame = false;

            displayCurrentGameConfig();
        }
    }

    private void setupGameConfigFieldsInput(String gameConfigStr, int poorScoreNum, int greatScoreNum) {

        // Get the name from user input
        // TODO: Validate string input
        try {
            gameConfigStr = gameConfigName.getText().toString();
        } catch (IllegalArgumentException ex) {
            //TODO: Empty catch for now, double check with team later
        }

        // Get the poor score from user input
        String poorScoreStr = poorScore.getText().toString();
        try {
            poorScoreNum = Integer.parseInt(poorScoreStr);
        } catch (NumberFormatException ex) {
            //TODO: Empty catch for now, double check with team later
        }

        // Get the great score from user input
        String greatScoreStr = greatScore.getText().toString();
        try {
            greatScoreNum = Integer.parseInt(greatScoreStr);
        } catch (NumberFormatException ex) {
            //TODO: Empty catch for now, double check with team later
        }

        // New Game! Set gamConfig object with user input values
        if (isNewGame) {
            newGameConfig.setConfigValues(gameConfigStr, poorScoreNum, greatScoreNum);
        } else {
            // Not a new Game!
            editedGameConfig.setConfigValues(gameConfigStr, poorScoreNum, greatScoreNum);
        }
    }

    private void displayCurrentGameConfig() {
        GameConfig currentGame = gameManager.getGameConfigByIndex(gameConfigPosition);

        gameConfigName.setText(currentGame.getName());
        poorScore.setText(String.valueOf(currentGame.getPoorScore()));
        greatScore.setText(String.valueOf(currentGame.getGreatScore()));
    }
}