package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AchievementSettingsActivity extends AppCompatActivity {

    private static final String APP_PREFS = "AppPrefs";

    public static Intent makeAchievementSettingsIntent(Context context) {
        Intent intent = new Intent(context, AchievementSettingsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_settings);
        setTitle(getString(R.string.achievement_settings_activity_title));

        RadioGroup settingsRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton adventurerThemeBtn = (RadioButton) findViewById(R.id.theme1RadioButton);
        RadioButton enchantedForestThemeBtn = (RadioButton) findViewById(R.id.theme2RadioButton);
        //TODO add 3rd radio btn

        setUpRadioBtnClick(adventurerThemeBtn);
        setUpRadioBtnClick(enchantedForestThemeBtn);

    }

    private void setUpRadioBtnClick(RadioButton btn) {
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(AchievementSettingsActivity.this, "Theme Changed", Toast.LENGTH_SHORT).show();
                saveTheme(btn);
            }
        });
    }

    private void saveTheme(RadioButton btn) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt()
    }


}