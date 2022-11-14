package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

/**Background photo from https://unsplash.com/photos/xplrF8WMitE, firework animation from https://www.pngegg.com/en/search?q=fireworks+clipart
 *
 */
public class EarnedAchievementActivity extends AppCompatActivity {

    private static int SPLASH_TIME = 6*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earned_achievement);
        setTitle("Congratulations!");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, SPLASH_TIME);
        Button skipBtn = (Button)findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}