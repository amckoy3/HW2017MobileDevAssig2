package com.example.mckoy.itunessearch;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity {
    private String mQuery;
    public final static String QUERY_KEY = "query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        View view = LayoutInflater.from(this).inflate(R.layout.edit_text, null);
        final EditText commentEditText = (EditText) view.findViewById(R.id.text_input);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Search for tracks");
                alertDialog.setView(commentEditText);
                alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mQuery = commentEditText.getText().toString();
                        /*getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                createCustomFragment(new SearchFragment(), mQuery)).commit();*/



                        FragmentManager fm = getSupportFragmentManager();

                        Fragment SearchFragment = new SearchFragment();
                            fm.beginTransaction().replace(R.id.container,
                                    createCustomFragment(new SearchFragment(), mQuery)).commit();
                        }


                    }
                );
                       alertDialog.setNegativeButton(android.R.string.cancel, null)
                        .create();

                alertDialog.show();

                return super.onOptionsItemSelected(menu);
        }




    private Fragment createCustomFragment(Fragment fragment, String query){
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_KEY, query);
        fragment.setArguments(bundle);
        return fragment;
    }
}

