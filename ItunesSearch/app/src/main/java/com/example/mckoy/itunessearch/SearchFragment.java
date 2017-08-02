package com.example.mckoy.itunessearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mckoy.itunessearch.Songs.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.example.mckoy.itunessearch.MainActivity.QUERY_KEY;

/**
 * Created by mckoy on 7/26/17.
 */

public class SearchFragment extends Fragment {

    private ItunesItemAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private String mCurrentlyPlayingUrl;
    private ListView mListView;
    private TextView mTextField;
    //private String mQuery;
    public final static String QUERY_KEY = "query";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.frag_itune_songs, container, false);
        mListView = v.findViewById(R.id.list_view);
        mTextField = (TextView)v.findViewById(R.id.text_view);
        mAdapter = new ItunesItemAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        String query = getArguments().getString(QUERY_KEY);
        //mTextField.append(query);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Songs song = (Songs) parent.getAdapter().getItem(position);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType(getString(R.string.setType));
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
                shareIntent.putExtra(Intent.EXTRA_TEXT, song.getTrackViewUrl());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
            }
        });

        ItunesSource.get(getContext()).getSongs(query, new ItunesSource.SongListener() {
            @Override
            public void onSongResponse(List<Songs> songList) {

                mAdapter.setItems(songList);
            }
        });

        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle b){
        super.onSaveInstanceState(b);
        b.putString("text", mTextField.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


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
        private NetworkImageView imageView;
        public ItunesItemAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<Songs> songsList) {
            mDataSource.clear();
            mDataSource.addAll(songsList);
            notifyDataSetChanged();
        }

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



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Songs song = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.list_songs, parent, false);
            final ImageView playButton = (ImageView) rowView.findViewById(R.id.imageView);
            final boolean isPlaying = mMediaPlayer.isPlaying() &&
                    mCurrentlyPlayingUrl.equals(song.getPreviewUrl());

            TextView songName = (TextView)rowView.findViewById(R.id.song);
            TextView artistName = (TextView)rowView.findViewById(R.id.artist);
            TextView albumName = (TextView)rowView.findViewById(R.id.album);
            NetworkImageView thumbnailField = (NetworkImageView)rowView.findViewById(R.id.thumbnail);

            songName.setText(song.getTrackName());
            artistName.setText(song.getArtistName());
            albumName.setText(song.getCollectionName());
            ImageLoader loader = ItunesSource.get(getContext()).getImageLoader();
            thumbnailField.setImageUrl(song.getArtworkUrl60(), loader);
            final String preview = song.getPreviewUrl();

            // Here, add code to set the play/pause button icon based on isPlaying
            playButton.setImageDrawable((isPlaying) ? getResources().getDrawable(android.R.drawable.ic_media_pause):
                    getResources().getDrawable(android.R.drawable.ic_media_play));

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedAudioURL(preview);
                }
            });


            return rowView;
        }


    }


}







