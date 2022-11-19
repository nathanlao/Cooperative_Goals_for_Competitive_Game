package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//SinglePlayerActivity records new user input to be saved back to
//RecordNewGamePlayActivity, inside of its Players List View,
//altering the value whenever required.
public class SinglePlayerActivity extends AppCompatActivity {

    private static final String SAVED_PLAYER_INPUT =
            "ca.cmpt276.chromiumproject - Saved integer input value by the user";
    private static final String POSITION_OF_THE_PLAYER =
            "ca.cmpt276.chromiumproject - Position of the player for name purpose";
    private static final int REQUEST_CODE_PLAYER_SCORE_INPUT = 101;

    private int extractedPlayerScore;
    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        setUpBackButton();

        extractDataFromIntent();
        setUpUserInterface();
    }

    public static Intent makeIntent (Context context, int curPos, int playerInput) {
        Intent intent = new Intent(context, SinglePlayerActivity.class);

        intent.putExtra(POSITION_OF_THE_PLAYER, curPos);
        intent.putExtra(SAVED_PLAYER_INPUT, playerInput);

        return intent;
    }

    public void extractDataFromIntent() {
        Intent intent = getIntent();
        extractedPlayerScore = intent.getIntExtra(SAVED_PLAYER_INPUT, 0);
        currentPosition = intent.getIntExtra(POSITION_OF_THE_PLAYER, 0);
    }
    public void setUpUserInterface() {
        TextView enterMsgText = findViewById(R.id.textViewEnterScoreMsg);
        enterMsgText.setText(getString(R.string.enter_player_info) +
                (currentPosition + 1));

        EditText userInputScoreEdit = findViewById(R.id.editTextSinglePlayer);
        userInputScoreEdit.setText(Integer.toString(extractedPlayerScore));

        Button singlePlayerSaveButton = findViewById(R.id.buttonSingleScoreConfirm);
        singlePlayerSaveButton.setText(getString(R.string.confirm_save_info));
        singlePlayerSaveButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            //EditText userInputScoreEdit2 = findViewById(R.id.editTextSinglePlayer);
            String savedUserIntInput = userInputScoreEdit.getText().toString();
            intent.putExtra(SAVED_PLAYER_INPUT, Integer.valueOf(savedUserIntInput));
            intent.putExtra(POSITION_OF_THE_PLAYER, currentPosition);
            setResult(REQUEST_CODE_PLAYER_SCORE_INPUT, intent);
            finish();
        });
    }

    public static int getPlayerResultMsg (Intent intent) {
        return intent.getIntExtra(SAVED_PLAYER_INPUT, 0);
    }
    public static int getPositionOfPlayer (Intent intent) {
        return intent.getIntExtra(POSITION_OF_THE_PLAYER, 0);
    }

    private void setUpBackButton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}