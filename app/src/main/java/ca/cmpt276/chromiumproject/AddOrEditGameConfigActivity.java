package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ca.cmpt276.chromiumproject.model.GameManager;

public class AddOrEditGameConfigActivity extends AppCompatActivity {

    private GameManager gameManager;

    public static final String EXTRA_EDIT_GAME_POSITION = "Edit Intent Extra - gameConfig position";

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

//                finish();
                return true;

            case android.R.id.home:
                Toast.makeText(this, "Game Config Not Saved!", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void extractPositionFromIntent() {
        Intent intent = getIntent();

        int gameConfigPosition = intent.getIntExtra(EXTRA_EDIT_GAME_POSITION, -1);

        if (gameConfigPosition == -1) {
            setTitle(getString(R.string.title_add_game_configs));
        } else {
            setTitle(getString(R.string.title_edit_game_configs));
        }
    }
}