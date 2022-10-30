package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}