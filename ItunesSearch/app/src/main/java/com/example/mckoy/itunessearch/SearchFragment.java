package com.example.mckoy.itunessearch;

import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mckoy on 7/26/17.
 */

public class SearchFragment extends Fragment {

    private ItunesItemAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private String mCurrentlyPlayingUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


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


    private void clickedAudioURL(String url) {
        if (mMediaPlayer.isPlaying()) {
            if (mCurrentlyPlayingUrl.equals(url)) {
                mMediaPlayer.stop();
                mAdapter.notifyDataSetChanged();
                return;
            }
        }

        mCurrentlyPlayingUrl = url;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mAdapter.notifyDataSetChanged();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mAdapter.notifyDataSetChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ItunesItemAdapter extends BaseAdapter {
        private Context mContext;
        private List<Songs> mDataSource;
        private LayoutInflater mInflater;
        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataSource.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public ItunesItemAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Songs song = mDataSource.get(position);
            boolean isPlaying = mMediaPlayer.isPlaying() &&
                    mCurrentlyPlayingUrl.equals(song.getPreviewUrl());

            // Here, add code to set the play/pause button icon based on isPlaying
            //playButton.setOnClickListener(new View.OnClickListener() {
               // @Override
               // public void onClick(View v) {
                 //   clickedAudioURL(item.getPreviewUrl());
               // }
           // });


            return convertView;
        }
    }


}







