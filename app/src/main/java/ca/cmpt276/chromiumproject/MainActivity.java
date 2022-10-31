package ca.cmpt276.chromiumproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    private boolean isEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: initialize array adapter before calling checkIsEmpty()
        if (isEmpty) {

            FragmentManager manager = getSupportFragmentManager();
            EmptyListFragment dialog = new EmptyListFragment();
            //TODO: add string to xml
            dialog.show(manager, "EmptyListFragment");

            TextView emptyText = findViewById(R.id.emptyText);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    private void checkIsEmpty(Adapter adapter) {
        if (adapter.getCount() == 0) {
            isEmpty = true;
        }
        else {
            isEmpty = false;
        }
    }

}