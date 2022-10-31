package ca.cmpt276.chromiumproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;



public class EmptyListFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create view to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.empty_list_fragment_message, null);

        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    getActivity();
                }
            }
        };
        //Build alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_welcome)
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }

}
