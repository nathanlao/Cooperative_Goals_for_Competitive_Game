package ca.cmpt276.chromiumproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chromiumproject.model.Difficulty;
import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

/** RecordNewGamePlayActivity records the # of players and combined score (inputted by users) and creates a new game record
 * The game record is added to the correct game config's list of game records and can be seen listed in the ViewGameConfigActivity
 */

public class RecordNewGamePlayActivity extends AppCompatActivity {

    public static final String EXTRA_RECORD_GAME_POSITION = "Record Intent Extra - gameConfig position";
    public static final String EXTRA_PAST_GAME_POSITION = "Past Game Intent Extra - pastGame position";

    public static final String TAG_NUMBER_FORMAT_EXCEPTION = "Catch NumberFormatException";
    public static final String TAG_ILLEGAL_ARGUMENT_EXCEPTION = "Catch IllegalArgumentException";
    private static final int REQUEST_CODE_PLAYER_SCORE_INPUT = 101;

    public static final int DEFAULT_PLAYER_COUNT = 2;

    private GameManager gameManager;
    private GameRecord gameRecord;
    private GameConfig gameConfig;

    private Difficulty selectedDifficulty;
    private Bitmap pictureTaken;

    private int gameConfigPosition;
    private int gamePlayPosition;

    private TextView numPlayersInput;
    private TextView combinedScore;

    private List<Integer> playerScoreList;
    private List<Integer> shadowPlayerScoreList;

    private boolean isNewGamePlay;

    public static Intent makeRecordIntent(Context context, int position) {
        Intent intent =  new Intent(context, RecordNewGamePlayActivity.class);
        intent.putExtra(EXTRA_RECORD_GAME_POSITION, position);
        return intent;
    }

    public static Intent makePastGameIntent(Context context, int gameConfigPosition, int gamePlayPosition) {
        Intent intent =  new Intent(context, RecordNewGamePlayActivity.class);
        intent.putExtra(EXTRA_RECORD_GAME_POSITION, gameConfigPosition);
        intent.putExtra(EXTRA_PAST_GAME_POSITION, gamePlayPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new_game_play);

        setUpBackButton();

        gameManager = GameManager.getInstance();

        setUpTextFields();
        setUpDifficultyButtons();

        extractGameConfigPositionFromIntent();
        extractPastGamePositionFromIntent();

        setUpNumPlayerSetButton();

        playerListClickSetUp();

        setUpDefaultImage();
        setUpTakePhotoFAB();

        initializeNewGameNumPlayer();
        initializeAllPlayerList();
    }

    private void initializeNewGameNumPlayer() {
        String theDefaultPlayerCount = Integer.toString(DEFAULT_PLAYER_COUNT);

        if (isNewGamePlay) {
            numPlayersInput.setText(theDefaultPlayerCount);
        }
    }
    private void initializeAllPlayerList() {
        int theDefaultInteger = Integer.parseInt(numPlayersInput.getText().toString());

        if (isNewGamePlay) {
            playerScoreList = new ArrayList<>();
            shadowPlayerScoreList = new ArrayList<>();

            for (int i = 0; i < theDefaultInteger; i++) {
                playerScoreList.add(0);
                shadowPlayerScoreList.add(0);
            }
        }

        renewPlayerList();
    }
    private void initializeShadowPlayer() {
        int theDefaultInteger = Integer.parseInt(numPlayersInput.getText().toString());

        shadowPlayerScoreList = new ArrayList<>();

        if (!isNewGamePlay) {
            for (int i = 0; i < theDefaultInteger; i++) {
                shadowPlayerScoreList.add(playerScoreList.get(i));
            }
        }

    }

    private void setUpDefaultImage() {
        ImageView gamePlayImage = findViewById(R.id.imgGamePlay);

        if (!isNewGamePlay) { // try loading existing image if it exists
            GameRecord currentGamePlay = gameConfig.getGameRecordByIndex(gamePlayPosition);
            Bitmap currentPhoto = PhotoHelper.loadBitmapPhotoFromModel(this, currentGamePlay);

            if (currentPhoto == null) {
                gamePlayImage.setImageResource(R.drawable.no_image_available);
            } else {
                gamePlayImage.setImageBitmap(currentPhoto);
                pictureTaken = currentPhoto;
            }
        } else {
            gamePlayImage.setImageResource(R.drawable.no_image_available);
        }
    }

    private void setUpTakePhotoFAB() {
        FloatingActionButton takePhoto = findViewById(R.id.fabTakePhoto);
        takePhoto.setOnClickListener(v -> {
            String userInputPlayerNumbers = numPlayersInput.getText().toString();
            String combinedScoreStr = combinedScore.getText().toString();

            // Validate if user has enter number of players or has enter the score
            if (TextUtils.isEmpty(userInputPlayerNumbers) || combinedScoreStr.matches(getString(R.string.default_combined_score))) {
                Toast.makeText(this, R.string.toast_set_number_of_players, Toast.LENGTH_SHORT).show();
            } else {
                // Launch image capture action to take photo
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> cameraActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Intent data = result.getData();
                            int resultCode = result.getResultCode();

                            if (resultCode == RESULT_OK && data != null) {
                                ImageView gamePlayImage = findViewById(R.id.imgGamePlay);
                                pictureTaken = (Bitmap) data.getExtras().get("data");
                                gamePlayImage.setImageBitmap(pictureTaken);
                            }
                        }
                    }
            );

    private void setUpNumPlayerSetButton() {
        Button numPlayerSetButton = findViewById(R.id.buttonNumPlayerSet);
        numPlayerSetButton.setOnClickListener(view -> renewPlayerList());
    }

    private void renewPlayerList() {
        ListView playersViewList = findViewById(R.id.listViewSinglePlayer);
        String userInputPlayerNumbers = numPlayersInput.getText().toString();
        int intConvertedUserInput = 0;
        //checkEmpty
        if (TextUtils.isEmpty(userInputPlayerNumbers)) {
            playersViewList.setAdapter(null);
        }
        if (!TextUtils.isEmpty(userInputPlayerNumbers)) {
            intConvertedUserInput = Integer.parseInt(userInputPlayerNumbers);
            //case of 0
            if (intConvertedUserInput == 0) {
                playersViewList.setAdapter(null);
                Toast.makeText(this, R.string.zero_player_msg, Toast.LENGTH_SHORT).show();
            }
            if (intConvertedUserInput > 0) {
                updatePlayerListData(intConvertedUserInput);
                populatePlayersListView();
                calibrateCombinedScore();
            }
        }
    }

    private void updatePlayerListData(int userIntInput) {
        List<Integer> tempListData = new ArrayList<>();

        //Check shadow list first
        //If it's actually big as user input, add if not
        int shadowSize = shadowPlayerScoreList.size();
        int theSizeDifference = userIntInput - shadowSize;
        if (shadowSize < userIntInput) {
            for (int i = 0; i < theSizeDifference; i++) {
                shadowPlayerScoreList.add(0);
            }
        }

        //Edit player list
        int curSize = playerScoreList.size();
        if (curSize > userIntInput) {
            for (int i = 0; i < userIntInput; i++) {
                tempListData.add(playerScoreList.get(i));
            }
        }
        if (curSize < userIntInput) {
            for (int i = 0; i < userIntInput; i++) {
                if (i < curSize) {
                    tempListData.add(playerScoreList.get(i));
                }
                else {
                    tempListData.add(shadowPlayerScoreList.get(i));
                }
            }
        }

        if (curSize != userIntInput) {
            playerScoreList = new ArrayList<>();
            for (int i = 0; i < tempListData.size(); i++) {
                int tempValue = tempListData.get(i);
                playerScoreList.add(tempValue);
            }

            //edit shadow list, either add or and copy values
            //from new set player list

            for (int i = 0; i < playerScoreList.size(); i++) {
                int tempValue = playerScoreList.get(i);
                shadowPlayerScoreList.set(i, tempValue);
            }
        }

    }

    private void populatePlayersListView() {
        ArrayAdapter<Integer> adapterPlayersList = new PlayerListAdapter();

        ListView playersViewList = findViewById(R.id.listViewSinglePlayer);
        playersViewList.setAdapter(adapterPlayersList);
    }

    private class PlayerListAdapter extends ArrayAdapter<Integer> {
        public PlayerListAdapter() {
            super(RecordNewGamePlayActivity.this, R.layout.player_view, playerScoreList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.player_view, parent, false);
            }

            String curPlayerName = getString(R.string.player_id_info)
                    + (position + 1);

            TextView curPlayerNameText = itemView.findViewById(R.id.item_player_name);
            curPlayerNameText.setText(curPlayerName);

            int curPlayerScore = playerScoreList.get(position);
            TextView curPlayerScoreText = itemView.findViewById(R.id.item_player_input_score);

            String curPlayerScoreMsg = getString(R.string.player_score_info);
            curPlayerScoreMsg += " " + curPlayerScore;

            curPlayerScoreText.setText(curPlayerScoreMsg);

            return itemView;
        }
    }
    private void playerListClickSetUp() {
        ListView playersViewList = findViewById(R.id.listViewSinglePlayer);
        playersViewList.setOnItemClickListener((parent, viewClicked, position, id) -> {
            int theSinglePlayerScore = playerScoreList.get(position);

            Intent intent = SetSinglePlayerScoreActivity.makeIntent(RecordNewGamePlayActivity.this,
                    position,
                    theSinglePlayerScore);
            playerActivityResultLauncher.launch(intent);

        });
    }

    ActivityResultLauncher<Intent> playerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    int resultCode = result.getResultCode();

                    if (resultCode == REQUEST_CODE_PLAYER_SCORE_INPUT) {
                        int newUserInputPlayerScore = SetSinglePlayerScoreActivity.getPlayerResultMsg(data);
                        int userPosition = SetSinglePlayerScoreActivity.getPositionOfPlayer(data);
                        playerScoreList.set(userPosition, newUserInputPlayerScore);
                        shadowPlayerScoreList.set(userPosition, newUserInputPlayerScore);

                        Log.i("PlayerListPart", "Activity SUCCESSFUL.");
                    }
                    calibrateCombinedScore();
                }
            }
    );

    private void calibrateCombinedScore() {
        int tempCombScore = 0;
        for (int i = 0; i < playerScoreList.size(); i++) {
            tempCombScore += playerScoreList.get(i);
        }

        String theCombText = Integer.toString(tempCombScore);
        combinedScore.setText(theCombText);
    }

    private void setUpDifficultyButtons() {
        Button normalBtn = findViewById(R.id.btnSelectNormal);
        Button easyBtn = findViewById(R.id.btnSelectEasy);
        Button hardBtn = findViewById(R.id.btnSelectHard);
        setDifficultyButtonsGray();

        normalBtn.setOnClickListener(v -> {
            setDifficultyButtonsGray();
            updateDifficultyButtons(Difficulty.NORMAL);
        });

        easyBtn.setOnClickListener(v -> {
            setDifficultyButtonsGray();
            updateDifficultyButtons(Difficulty.EASY);
        });

        hardBtn.setOnClickListener(v -> {
            setDifficultyButtonsGray();
            updateDifficultyButtons(Difficulty.HARD);
        });
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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpTextFields() {
        numPlayersInput = findViewById(R.id.numPlayersInput);
        combinedScore = findViewById(R.id.textViewCombinedScore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu
        getMenuInflater().inflate(R.menu.menu_record_new_game_played, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save:
                //Fix if player count set in Textview and actual count mismatch
                checkPlayerCountMismatch();

                // Validate difficulty buttons, validate non-empty input, validate set button (playerScoreList must be not null)
                if (checkNullSelectedDifficulty() || checkEmptyInput() || checkInvalidInput() || checkNullPlayerScoreList()) {
                    return false;
                }

                // if API version <29, need to request permissions to write to external storage
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    if(ContextCompat.checkSelfPermission(RecordNewGamePlayActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        saveGameRecordAndFinish();
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
                else {
                    saveGameRecordAndFinish();
                }

                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkPlayerCountMismatch() {
        int curTextViewPlayerInteger = Integer.parseInt(numPlayersInput.getText().toString());
        int curSetPlayerListDataSize = playerScoreList.size();

        //Fix if textview value mismatch to actual player score list size
        //Caused by player not setting after changing textview value
        if (curTextViewPlayerInteger != curSetPlayerListDataSize) {
            curTextViewPlayerInteger = curSetPlayerListDataSize;
        }

        numPlayersInput.setText("" + curTextViewPlayerInteger);
    }

    private void saveGameRecordAndFinish() {
        // Take user input
        setupGameRecordInput();

        // save photo as File
        savePictureTaken();

        // save updated gameConfigs list to SharedPrefs
        MainActivity.saveGameConfigs(this, gameManager);

        if (isNewGamePlay) {
            setUpEarnedAchievement();
        }

        Toast.makeText(this, R.string.toast_save_game_record, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void savePictureTaken() {
        if (pictureTaken != null) {
            PhotoHelper.savePhotoAndStoreInModel(RecordNewGamePlayActivity.this, gameRecord, pictureTaken);
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    saveGameRecordAndFinish();
                } else { // save fails until permissions are allowed
                    String writeDeniedMsg = getString(R.string.write_access_denied_msg);
                    Toast.makeText(RecordNewGamePlayActivity.this, writeDeniedMsg, Toast.LENGTH_LONG).show();
                }
            });

    private void setupGameRecordInput() {
        // Take user input and get current gameConfig
        int numberOfPlayersNum = 0;
        int combinedScoreNum = 0;
        gameConfig = gameManager.getGameConfigByIndex(gameConfigPosition);

        String  numberOfPlayersStr = numPlayersInput.getText().toString();
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
                gameRecord = new GameRecord(numberOfPlayersNum, combinedScoreNum, gameConfig.getPoorScore(), gameConfig.getGreatScore(), selectedDifficulty);

                // Copy over the player score list into gameRecord's playerScoreList
                for (int playerScore : playerScoreList) {
                    gameRecord.addPlayerScore(playerScore);
                }
                gameConfig.addGameRecord(gameRecord);

            } catch (IllegalArgumentException ex) {
                Log.d(TAG_ILLEGAL_ARGUMENT_EXCEPTION, "IllegalArgumentException caught: number of players must be greater than 0");
            }
        } else {
            try {
                // Get current game record and edit it
                gameRecord = gameConfig.getGameRecordByIndex(gamePlayPosition);
                gameRecord.editGameRecordValues(numberOfPlayersNum, combinedScoreNum, gameConfig.getPoorScore(), gameConfig.getGreatScore(), selectedDifficulty);
                // Update playerScoreList after user editing the game
                gameRecord.setPlayerScoreList(playerScoreList);
                gameConfig.setGameRecordByIndex(gamePlayPosition, gameRecord);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG_ILLEGAL_ARGUMENT_EXCEPTION, "IllegalArgumentException caught: number of players must be greater than 0");
            }
        }
    }

    private boolean checkEmptyInput() {
        String numOfPlayersStr = numPlayersInput.getText().toString();
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
        String numOfPlayersStr = numPlayersInput.getText().toString();

        if (numOfPlayersStr.matches("0")) {
            Toast.makeText(this, R.string.check_invalid_number_of_player, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean checkNullPlayerScoreList() {
        if (playerScoreList == null) {
            Toast.makeText(this, R.string.toast_set_single_player_score, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public void setUpEarnedAchievement(){
        String combinedScoreStr = combinedScore.getText().toString();
        int combinedScorePoints = Integer.parseInt(combinedScoreStr);

        Intent i = EarnedAchievementActivity.makeEarnedAchievementIntent(RecordNewGamePlayActivity.this, gameConfigPosition, combinedScorePoints);
        startActivity(i);
    }

    private boolean checkNullSelectedDifficulty() {
        if (selectedDifficulty == null) {
            Toast.makeText(this, getString(R.string.null_difficulty_selected_error), Toast.LENGTH_LONG).show();
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
        gameConfigPosition = pastGameIntent.getIntExtra(EXTRA_RECORD_GAME_POSITION, 0);
        gamePlayPosition = pastGameIntent.getIntExtra(EXTRA_PAST_GAME_POSITION, -1);

        if (gamePlayPosition == -1) {
            isNewGamePlay = true;
        } else {
            GameConfig currentGameConfigs = gameManager.getGameConfigByIndex(gameConfigPosition);
            setTitle(getString(R.string.edit_game_play_title) + " " + currentGameConfigs.getName());

            isNewGamePlay = false;
            displayCurrentGamePlay();
        }
    }

    private void displayCurrentGamePlay() {
        // Getting current gameConfigs and its associated gameRecord
        gameConfig = gameManager.getGameConfigByIndex(gameConfigPosition);
        GameRecord currentGamePlay = gameConfig.getGameRecordByIndex(gamePlayPosition);
        Difficulty currentSelectedDifficulty = currentGamePlay.getDifficulty();

        setUpDefaultImage();

        updateDifficultyButtons(currentSelectedDifficulty);

        numPlayersInput.setText(String.valueOf(currentGamePlay.getNumPlayers()));
        combinedScore.setText(String.valueOf(currentGamePlay.getCombinedScore()));

        // Get player score list from current gameRecord
        playerScoreList = currentGamePlay.getPlayerScoreList();

        initializeShadowPlayer();

        // Update on playerScoreList based on input player count
        String userInputPlayerNumbers = numPlayersInput.getText().toString();
        int intConvertedUserInput = Integer.parseInt(userInputPlayerNumbers);
        updatePlayerListData(intConvertedUserInput);
        populatePlayersListView();
    }

    private void updateDifficultyButtons(Difficulty difficulty) {
        Button normalBtn = findViewById(R.id.btnSelectNormal);
        Button easyBtn = findViewById(R.id.btnSelectEasy);
        Button hardBtn = findViewById(R.id.btnSelectHard);

        switch(difficulty) {
            case NORMAL:
                // Assign values to global selectedDifficulty, to avoid gameRecord working on null selectedDifficulty
                selectedDifficulty = difficulty;
                normalBtn.setBackgroundColor(Color.BLUE);
                break;
            case EASY:
                selectedDifficulty = difficulty;
                easyBtn.setBackgroundColor(Color.GREEN);
                break;
            case HARD:
                selectedDifficulty = difficulty;
                hardBtn.setBackgroundColor(Color.RED);
                break;
            default:
                setDifficultyButtonsGray();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playerScoreList != null) {
            populatePlayersListView();
        }
    }
}