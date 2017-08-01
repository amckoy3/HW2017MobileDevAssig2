package com.example.mckoy.itunessearch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by mckoy on 7/26/17.
 */

public class SearchFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);


        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_text, null);
        final EditText commentEditText = (EditText) view.findViewById(R.id.text_input);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Search for tracks")
                .setView(commentEditText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String query = commentEditText.getText().toString();
                        Log.i("DEI", query);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alertDialog.show();
        return super.onOptionsItemSelected(menu);
    }
}




