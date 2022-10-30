package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AddOrEditGameConfigActivity extends AppCompatActivity {

    public static Intent makeAddIntent(Context context) {
        return new Intent(context, AddOrEditGameConfigActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_game_config);
    }
}