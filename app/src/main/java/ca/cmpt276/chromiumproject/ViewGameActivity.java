package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        setUpEditConfig();
    }

    private void setUpEditConfig() {
        Button editBtn = (Button) findViewById(R.id.editConfigBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send edit intent to edit screen
                Intent editIntent = new Intent(ViewGameActivity.this, MainActivity.class);
                startActivity(editIntent);
            }
        });
    }

    private void setUpRecordNewGame() {
        Button recordBtn = (Button) findViewById(R.id.recordGameBtn);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send edit intent to record game screen
                Intent recordGameIntent = new Intent(ViewGameActivity.this, MainActivity.class);
                startActivity(recordGameIntent);
            }
        });
    }

    



}