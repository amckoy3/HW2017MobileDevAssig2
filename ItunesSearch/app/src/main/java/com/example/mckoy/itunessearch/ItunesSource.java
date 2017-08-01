package com.example.mckoy.itunessearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mckoy on 8/1/17.
 */

public class ItunesSource {

    public interface SongListener {
        void onSongResponse(List<Songs> iTunesList);
    }

    private final static int IMAGE_CACHE_COUNT = 100;
    private final static int iTunes_REQUEST_COUNT = 25;
    private final static int iTunes_REQUEST_IMAGE_WIDTH = 400;
    private static ItunesSource sItunesSourceInstance;

    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static ItunesSource get(Context context) {
        if (sItunesSourceInstance == null) {
            sItunesSourceInstance = new ItunesSource(context);
        }
        return sItunesSourceInstance;
    }

    private ItunesSource(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(IMAGE_CACHE_COUNT);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public void getSongs(SongListener songListener) {
        final SongListener songListenerInternal = songListener;
        // URL for requesting articles from Wikipedia. This requests N random articles, with a short
        // text extract (rather than the whole article text), with a main thumbnail image of size M,
        // and with metadata including a link to the wikipedia article itself.
        String url = "https://itunes.apple.com/search?term=<your search term>&entity=musicTrack";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Songs> songList = new ArrayList<Songs>();
                            // Get the map of articles, keyed by article id.
                            JSONObject songObj = response.getJSONObject("query").getJSONObject("pages");
                            Iterator<String> it = songObj.keys();
                            while (it.hasNext()) {
                                String key = it.next();
                                JSONObject songObject = songObj.getJSONObject(key);
                                Songs isong = new Songs(songObject);
                                songList.add(isong);
                            }
                            songListenerInternal.onSongResponse(songList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            songListenerInternal.onSongResponse(null);
                            Toast.makeText(mContext, "Could not find song.", Toast.LENGTH_SHORT);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        songListenerInternal.onSongResponse(null);
                        Toast.makeText(mContext, "Could not find song.", Toast.LENGTH_SHORT);
                    }
                });

        mRequestQueue.add(jsonObjRequest);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
