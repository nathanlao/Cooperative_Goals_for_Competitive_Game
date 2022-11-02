package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;

/**
 * AddOrEditGameConfigActivity class allows user to add a new game config or edit current game config
 * Activity displays name, poor score and great score
 * Up button return to MainActivity and not save the game config
 * Save button save a new game config or current edited game config
 */

public class AddOrEditGameConfigActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_GAME_POSITION = "Edit Intent Extra - gameConfig position";

    public static final String TAG_NUMBER_FORMAT_EXCEPTION = "Catch NumberFormatException";
    public static final String TAG_ILLEGAL_ARGUMENT_EXCEPTION = "Catch NumberFormatException";

    private GameManager gameManager;

    private GameConfig newGameConfig;
    private GameConfig editedGameConfig;

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save_game_config:

                String gameConfigStr = "";
                int poorScoreNum = 0;
                int greatScoreNum = 0;

                // Take in user input
                setupGameConfigFieldsInput(gameConfigStr, poorScoreNum, greatScoreNum);

                // Validate empty input and display Toast message accordingly
                if (checkEmptyInput()) {
                    return false;
                }

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
        try {
            gameConfigStr = gameConfigName.getText().toString();
        } catch (NumberFormatException ex) {
            // Debugging purpose
            Log.d(TAG_NUMBER_FORMAT_EXCEPTION, "NumberFormatException caught: Game Config name must not be empty.");
        }

        // Get the poor score from user input
        String poorScoreStr = poorScore.getText().toString();
        try {
            poorScoreNum = Integer.parseInt(poorScoreStr);
        } catch (NumberFormatException ex) {
            Log.d(TAG_NUMBER_FORMAT_EXCEPTION, "NumberFormatException caught: Game Config poor score must not be empty.");
        }

        // Get the great score from user input
        String greatScoreStr = greatScore.getText().toString();
        try {
            greatScoreNum = Integer.parseInt(greatScoreStr);
        } catch (NumberFormatException ex) {
            Log.d(TAG_NUMBER_FORMAT_EXCEPTION, "NumberFormatException caught: Game Config great score must not be empty.");
        }

        // New Game! Set gamConfig object with user input values
        if (isNewGame) {
            try {
                newGameConfig = new GameConfig(gameConfigStr, poorScoreNum, greatScoreNum);
                newGameConfig.setConfigValues(gameConfigStr, poorScoreNum, greatScoreNum);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG_ILLEGAL_ARGUMENT_EXCEPTION, "IllegalArgumentException caught: Game Config name must not be empty.");
            }

        } else {
            try {
                // Not a new Game!
                editedGameConfig = new GameConfig(gameConfigStr, poorScoreNum, greatScoreNum);
                editedGameConfig.setConfigValues(gameConfigStr, poorScoreNum, greatScoreNum);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG_ILLEGAL_ARGUMENT_EXCEPTION, "IllegalArgumentException caught: Game Config name must not be empty.");
            }
        }
    }

    private boolean checkEmptyInput() {
        String gameConfigStr = gameConfigName.getText().toString();
        String poorScoreStr = poorScore.getText().toString();
        String greatScoreStr = greatScore.getText().toString();

        // TODO: Probably Validate String input for name field

        if (gameConfigStr.matches("")) {
            Toast.makeText(this, "All fields cannot be empty, please enter the Game Name", Toast.LENGTH_LONG).show();
            return true;
        } else if (poorScoreStr.matches("")) {
            Toast.makeText(this, "All fields cannot be empty, please enter the Poor Score", Toast.LENGTH_LONG).show();
            return true;
        } else if (greatScoreStr.matches("")) {
            Toast.makeText(this, "All fields cannot be empty, please enter the Great Score", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void displayCurrentGameConfig() {
        GameConfig currentGame = gameManager.getGameConfigByIndex(gameConfigPosition);

        gameConfigName.setText(currentGame.getName());
        poorScore.setText(String.valueOf(currentGame.getPoorScore()));
        greatScore.setText(String.valueOf(currentGame.getGreatScore()));
    }
}