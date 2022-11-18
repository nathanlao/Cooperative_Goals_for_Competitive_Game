package ca.cmpt276.chromiumproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chromiumproject.model.Achievement;
import ca.cmpt276.chromiumproject.model.Difficulty;
import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

/** RecordNewGamePlayActivity records the # of players and combined score (inputted by users) and creates a new game record
 * The game record is added to the correct game config's list of game records and can be seen listed in the ViewGameConfigActivity
 */

public class RecordNewGamePlayActivity extends AppCompatActivity {

    public static final String EXTRA_RECORD_GAME_POSITION = "Record Intent Extra - gameConfig position";

    public static final String TAG_NUMBER_FORMAT_EXCEPTION = "Catch NumberFormatException";
    public static final String TAG_ILLEGAL_ARGUMENT_EXCEPTION = "Catch IllegalArgumentException";
    private static final int REQUEST_CODE_PLAYER_SCORE_INPUT = 101;

    private GameManager gameManager;
    private GameRecord gameRecord;
    private GameConfig gameConfigs;

    private Difficulty selectedDifficulty;

    private int gameConfigPosition;

    private TextView numPlayers;
    private TextView combinedScore;

    private List<Integer> playerListData;

    public static Intent makeRecordIntent(Context context, int position) {
        Intent intent =  new Intent(context, RecordNewGamePlayActivity.class);
        intent.putExtra(EXTRA_RECORD_GAME_POSITION, position);
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

        extractPositionFromIntent();
        
        setUpTextMonitor();

        playerListClickSetUp();

    }

    //Monitor input change on NumPlayer Edit text
    private void setUpTextMonitor() {
        numPlayers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                renewPlayerList();
            }
        });
    }

    private void renewPlayerList() {
        ListView playersViewList = findViewById(R.id.listViewSinglePlayer);
        String userInputPlayerNumbers = numPlayers.getText().toString();
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

        //modified to preserve data once Player size changes
        if (playerListData == null) {
            playerListData = new ArrayList<>();

            for (int i = 0; i < userIntInput; i++) {
                playerListData.add(0);
            }
        }
        else if (playerListData != null) {
            int curSize = playerListData.size();
            if (curSize > userIntInput) {
                for (int i = 0; i < userIntInput; i++) {
                    tempListData.add(playerListData.get(i));
                }
            }
            if (curSize < userIntInput) {
                for (int i = 0; i < userIntInput; i++) {
                    if (i < curSize) {
                        tempListData.add(playerListData.get(i));
                    }
                    else {
                        tempListData.add(0);
                    }
                }
            }

            playerListData = new ArrayList<>();
            for (int i = 0; i < tempListData.size(); i++) {
                int tempValue = tempListData.get(i);
                playerListData.add(tempValue);
            }
            //playerListData = tempListData;
        }
    }

    private boolean isNewPlayerList() {
        boolean theResult = true;

        for(int i = 0; i < playerListData.size(); i++) {
            if (playerListData.get(i) != 0) {
                theResult = false;
            }
        }

        return theResult;
    }

    private void populatePlayersListView() {
        ArrayAdapter<Integer> adapterPlayersList = new PlayerListAdapter();

        ListView playersViewList = findViewById(R.id.listViewSinglePlayer);
        playersViewList.setAdapter(adapterPlayersList);
    }
    private class PlayerListAdapter extends ArrayAdapter<Integer> {
        public PlayerListAdapter() {
            super(RecordNewGamePlayActivity.this, R.layout.player_view, playerListData);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.player_view, parent, false);
            }

            String curPlayerName = getString(R.string.player_id_info)
                    + Integer.toString(position);

            TextView curPlayerNameText = itemView.findViewById(R.id.item_player_name);
            curPlayerNameText.setText(curPlayerName);

            int curPlayerScore = playerListData.get(position);
            TextView curPlayerScoreText = itemView.findViewById(R.id.item_player_input_score);

            String curPlayerScoreMsg = getString(R.string.player_score_info);
            curPlayerScoreMsg += " " + curPlayerScore;

            curPlayerScoreText.setText(curPlayerScoreMsg);

            return itemView;
        }
    }
    private void playerListClickSetUp() {
        ListView playersViewList = findViewById(R.id.listViewSinglePlayer);
        playersViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                int clickedPos = position;
                int theSinglePlayerScore = playerListData.get(position);

                Intent intent = SinglePlayerActivity.makeIntent(RecordNewGamePlayActivity.this,
                        clickedPos,
                        theSinglePlayerScore);
                playerActivityResultLauncher.launch(intent);
            }
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
                        int newUserInputPlayerScore = SinglePlayerActivity.getPlayerResultMsg(data);
                        int userPosition = SinglePlayerActivity.getPositionOfPlayer(data);
                        playerListData.set(userPosition, newUserInputPlayerScore);
                        Log.i("PlayerListPart", "Activity SUCCESSFUL.");
                    }
                    calibrateCombinedScore();
                }
            }
    );
    private void calibrateCombinedScore() {
        int tempCombScore = 0;
        for (int i = 0; i < playerListData.size(); i++) {
            tempCombScore += playerListData.get(i);
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
            selectedDifficulty = Difficulty.NORMAL;
            setDifficultyButtonsGray();
            normalBtn.setBackgroundColor(Color.BLUE);
        });

        easyBtn.setOnClickListener(v -> {
            selectedDifficulty = Difficulty.EASY;
            setDifficultyButtonsGray();
            easyBtn.setBackgroundColor(Color.GREEN);
        });

        hardBtn.setOnClickListener(v -> {
            selectedDifficulty = Difficulty.HARD;
            setDifficultyButtonsGray();
            hardBtn.setBackgroundColor(Color.RED);
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
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpInputFields() {
        numPlayers = findViewById(R.id.numPlayersInput);
        combinedScore = findViewById(R.id.combinedScoreInput);
    }

    private void extractPositionFromIntent() {
        Intent intent = getIntent();
        gameConfigPosition = intent.getIntExtra(EXTRA_RECORD_GAME_POSITION, 0);
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
                // Validate difficulty buttons. Display error if no difficulty is selected.
                if (checkNullSelectedDifficulty()) {
                    return false;
                }

                // Take user input
                setupGameRecordInput();

                // Validate empty input and display Toast message accordingly
                if (checkEmptyInput() || checkInvalidInput()) {
                    return false;
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

        // Add game record to the record list in gameConfig
        try {
            gameRecord = new GameRecord(numberOfPlayersNum, combinedScoreNum, gameConfigs.getPoorScore(), gameConfigs.getGreatScore(), selectedDifficulty);
            gameConfigs.addGameRecord(gameRecord);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG_ILLEGAL_ARGUMENT_EXCEPTION, "IllegalArgumentException caught: number of players must be greater than 0");
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

    private boolean checkNullSelectedDifficulty() {
        if (selectedDifficulty == null) {
            Toast.makeText(this, getString(R.string.null_difficulty_selected_error), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}