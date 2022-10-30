package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddOrEditGameConfigActivity extends AppCompatActivity {

    public static Intent makeAddIntent(Context context) {
        return new Intent(context, AddOrEditGameConfigActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_game_config);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}