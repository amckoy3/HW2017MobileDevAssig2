package com.example.mckoy.itunessearch;

/**
 * Created by mckoy on 8/1/17.
 */
import org.json.JSONException;
import org.json.JSONObject;

public class Songs {
    protected String kind;
    protected String trackName;
    protected String artistName;
    protected String collectionName;
    protected String previewUrl;
    protected String artworkUrl60;
    protected String trackViewUrl;

    public Songs(JSONObject articleObj) {
        try {
            // We expect that these two keys will be in the response.
            kind = articleObj.getString("content");
            trackName = articleObj.getString("track");
            artistName = articleObj.getString("artist");
            collectionName = articleObj.getString("albumTitle");
            previewUrl = articleObj.getString("preview");
            trackViewUrl = articleObj.getString("trackView");

            // There may or may not be a body text snippet in the response, so check before trying
            // to use it.
            if (articleObj.has("artwork")) {
                artworkUrl60 = articleObj.getString("artwork");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getKind() { return kind; }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getPreviewUrl() { return previewUrl; }

    public String getArtworkUrl60() {
        return artworkUrl60;
    }

    public String getTrackViewUrl() {
        return trackViewUrl;
    }

}

