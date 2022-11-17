package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

/** RecordNewGamePlayActivity records the # of players and combined score (inputted by users) and creates a new game record
 * The game record is added to the correct game config's list of game records and can be seen listed in the ViewGameConfigActivity
 */

public class RecordNewGamePlayActivity extends AppCompatActivity {

    public static final String EXTRA_RECORD_GAME_POSITION = "Record Intent Extra - gameConfig position";
    public static final String EXTRA_PAST_GAME_POSITION = "Past Game Intent Extra - pastGame position";
    public static final String EXTRA_PAST_GAME_CONFIG_POSITION = "Past Game Config Intent Extra - pastGameConfig position";

    public static final String TAG_NUMBER_FORMAT_EXCEPTION = "Catch NumberFormatException";
    public static final String TAG_ILLEGAL_ARGUMENT_EXCEPTION = "Catch IllegalArgumentException";

    private GameManager gameManager;
    private GameRecord gameRecord;
    private GameConfig gameConfigs;

    // Position for makeRecordIntent
    private int gameConfigPosition;

    // Positions for makePastGameIntent
    private int currentGameConfigPosition;
    private int currentGamePlayPosition;

    private TextView numPlayers;
    private TextView combinedScore;

    private boolean isNewGamePlay;

    public static Intent makeRecordIntent(Context context, int position) {
        Intent intent =  new Intent(context, RecordNewGamePlayActivity.class);
        intent.putExtra(EXTRA_RECORD_GAME_POSITION, position);
        return intent;
    }

    public static Intent makePastGameIntent(Context context, int gameConfigPosition, int gamePlayPosition) {
        Intent intent =  new Intent(context, RecordNewGamePlayActivity.class);
        intent.putExtra(EXTRA_PAST_GAME_CONFIG_POSITION, gameConfigPosition);
        intent.putExtra(EXTRA_PAST_GAME_POSITION, gamePlayPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new_game_play);

        setUpBackButton();

        gameManager = GameManager.getInstance();

        setUpInputFields();
        setUpDifficultyButtons();

        extractGameConfigPositionFromIntent();
        extractPastGamePositionFromIntent();
    }

    private void setUpDifficultyButtons() {
        Button normalBtn = findViewById(R.id.btnSelectNormal);
        Button easyBtn = findViewById(R.id.btnSelectEasy);
        Button hardBtn = findViewById(R.id.btnSelectHard);
        setDifficultyButtonsGray();

        normalBtn.setOnClickListener(v -> {
            setDifficultyButtonsGray();
            normalBtn.setBackgroundColor(Color.BLUE);
            // TODO: Testing purpose, delete later
            Toast.makeText(RecordNewGamePlayActivity.this, "Testing: normal", Toast.LENGTH_SHORT).show();
        });

        easyBtn.setOnClickListener(v -> {
            setDifficultyButtonsGray();
            easyBtn.setBackgroundColor(Color.GREEN);
            // TODO: Testing purpose, delete later
            Toast.makeText(RecordNewGamePlayActivity.this, "Testing: easy", Toast.LENGTH_SHORT).show();
        });

        hardBtn.setOnClickListener(v -> {
            setDifficultyButtonsGray();
            hardBtn.setBackgroundColor(Color.RED);
            // TODO: Testing purpose, delete later
            Toast.makeText(RecordNewGamePlayActivity.this, "Testing: hard", Toast.LENGTH_SHORT).show();
        });

        // TODO: throw error if no difficulty is selected for new game play save
    }

    private void setDifficultyButtonsGray() {
        Button normalBtn = findViewById(R.id.btnSelectNormal);
        Button easyBtn = findViewById(R.id.btnSelectEasy);
        Button hardBtn = findViewById(R.id.btnSelectHard);

        normalBtn.setBackgroundColor(Color.GRAY);
        easyBtn.setBackgroundColor(Color.GRAY);
        hardBtn.setBackgroundColor(Color.GRAY);
    }

    private void setUpBackButton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpInputFields() {
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

                // Validate empty input and display Toast message accordingly
                if (checkEmptyInput() || checkInvalidInput()) {
                    return false;
                }

                // Take user input
                setupGameRecordInput();

                if (isNewGamePlay) {
                    gameConfigs.addGameRecord(gameRecord);
                } else {
                    gameConfigs.setGameRecordByIndex(currentGamePlayPosition, gameRecord);
                }

                // save updated gameConfigs list to SharedPrefs
                MainActivity.saveGameConfigs(this, gameManager);
                Toast.makeText(this, R.string.toast_save_game_record, Toast.LENGTH_SHORT).show();
                finish();

                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupGameRecordInput() {

        // Take user input and get current gameConfig
        int numberOfPlayersNum = 0;
        int combinedScoreNum = 0;
        gameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);

        String  numberOfPlayersStr = numPlayers.getText().toString();
        try {
            numberOfPlayersNum = Integer.parseInt(numberOfPlayersStr);
        } catch (NumberFormatException ex) {
            Log.d(TAG_NUMBER_FORMAT_EXCEPTION, "NumberFormatException caught: number of players can not be empty");
        }

        String combinedScoreStr = combinedScore.getText().toString();
        try {
            combinedScoreNum = Integer.parseInt(combinedScoreStr);
        } catch (NumberFormatException ex) {
            Log.d(TAG_NUMBER_FORMAT_EXCEPTION, "NumberFormatException caught: combined score can not be empty");
        }

        if (isNewGamePlay) {
            // Add new game record to the record list in gameConfig
            try {
                gameRecord = new GameRecord(numberOfPlayersNum, combinedScoreNum, gameConfigs.getPoorScore(), gameConfigs.getGreatScore());
            } catch (IllegalArgumentException ex) {
                Log.d(TAG_ILLEGAL_ARGUMENT_EXCEPTION, "IllegalArgumentException caught: number of players must be greater than 0");
            }
        } else {
            try {
                // Get current game configs and edit its associated game record
                gameConfigs = gameManager.getGameConfigByIndex(currentGameConfigPosition);
                gameRecord = gameConfigs.getGameRecordByIndex(currentGamePlayPosition);

                // TODO: Need to change gameRecord fields (combinedScoreNum) when score calculator part is done
                gameRecord.setGameRecordFields(numberOfPlayersNum, combinedScoreNum, gameConfigs.getPoorScore(), gameConfigs.getGreatScore());

            } catch (IllegalArgumentException ex) {
                Log.d(TAG_ILLEGAL_ARGUMENT_EXCEPTION, "IllegalArgumentException caught: number of players must be greater than 0");
            }
        }
    }

    private boolean checkEmptyInput() {
        String numOfPlayersStr = numPlayers.getText().toString();
        String combinedScoreStr = combinedScore.getText().toString();

        if (numOfPlayersStr.matches("")) {
            Toast.makeText(this, R.string.toast_check_empty_num_players, Toast.LENGTH_LONG).show();
            return true;
        } else if (combinedScoreStr.matches("")){
            Toast.makeText(this, R.string.toast_check_empty_combine_score, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean checkInvalidInput() {
        String numOfPlayersStr = numPlayers.getText().toString();

        if (numOfPlayersStr.matches("0")) {
            Toast.makeText(this, R.string.check_invalid_number_of_player, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void extractGameConfigPositionFromIntent() {
        Intent gameConfigIntent = getIntent();
        gameConfigPosition = gameConfigIntent.getIntExtra(EXTRA_RECORD_GAME_POSITION, 0);
    }

    private void extractPastGamePositionFromIntent() {
        Intent pastGameIntent = getIntent();
        currentGameConfigPosition = pastGameIntent.getIntExtra(EXTRA_PAST_GAME_CONFIG_POSITION, 0);
        currentGamePlayPosition = pastGameIntent.getIntExtra(EXTRA_PAST_GAME_POSITION, -1);

        if (currentGamePlayPosition == -1) {
            isNewGamePlay = true;
        } else {
            GameConfig currentGameConfigs = gameManager.getGameConfigByIndex(currentGameConfigPosition);
            setTitle(getString(R.string.edit_game_play_title) + " " + currentGameConfigs.getName());
            isNewGamePlay = false;
            displayCurrentGamePlay();
        }
    }

    private void displayCurrentGamePlay() {
        // Getting current gameConfigs and its associated gameRecord
        gameConfigs = gameManager.getGameConfigByIndex(currentGameConfigPosition);
        GameRecord currentGamePlay = gameConfigs.getGameRecordByIndex(currentGamePlayPosition);

        // TODO: Probably need to reflect on button select as well

        numPlayers.setText(String.valueOf(currentGamePlay.getNumPlayers()));

        // TODO: Need to change this when score calculator part is done
        combinedScore.setText(String.valueOf(currentGamePlay.getCombinedScore()));
    }
}