package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

/**Achievement Settings allows users to choose which theme they would like to view their Achievement Levels with.
 * Selecting a theme changes the achievement levels in the list of past games played for all past games in all game configs,
 * the achievement names shown in the View Achievement Screen and the Earned Achievement pop up message.
 */
public class AchievementSettingsActivity extends AppCompatActivity {

    private static final String APP_PREFS = "AppPrefs";
    private static final String THEME = "Theme";
    private RadioGroup settingsRadioGroup;
    private RadioButton checkedRadioButton;

    public static Intent makeAchievementSettingsIntent(Context context) {
        Intent intent = new Intent(context, AchievementSettingsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_settings);
        setTitle(getString(R.string.achievement_settings_activity_title));

        setUpBackButton();

        settingsRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        createRadioButtons();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu
        getMenuInflater().inflate(R.menu.menu_achievement_settings, menu);
        return true;
    }

    private void setUpBackButton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void saveTheme(String theme) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(THEME, theme);
        editor.apply();
    }

    public static String getTheme(Context  c) {
        SharedPreferences prefs = c.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        String defaultTheme = c.getResources().getString(R.string.default_theme);
        return prefs.getString(THEME, defaultTheme);
    }

    private void createRadioButtons() {
        String[] themeOptions = getResources().getStringArray(R.array.theme_names);

        //Create radio btns
        for (int i = 0; i<themeOptions.length; i++) {
            final String themeName = themeOptions[i];

            RadioButton radioThemeBtn = new RadioButton(this);
            radioThemeBtn.setText(themeName);

            radioThemeBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Toast.makeText(AchievementSettingsActivity.this, "Theme Changed", Toast.LENGTH_SHORT).show();
                    saveTheme(themeName);
                }
            });
            settingsRadioGroup.addView(radioThemeBtn);
            if (Objects.equals(themeName, getTheme(this))) {
                radioThemeBtn.setChecked(true);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save_achievement_settings:

                String savedTheme = getTheme(AchievementSettingsActivity.this);
                saveTheme(savedTheme);
                finish();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}