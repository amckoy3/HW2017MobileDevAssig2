package com.example.mckoy.itunessearch;

/**
 * Created by mckoy on 8/1/17.
 */
import org.json.JSONException;
import org.json.JSONObject;

public class Songs {
    protected String mkind;
    protected String mtrackName;
    protected String martistName;
    protected String mcollectionName;
    protected String mpreviewUrl;
    protected String martworkUrl60;
    protected String mtrackViewUrl;

    public Songs(JSONObject articleObj) {
        try {
            // We expect that these two keys will be in the response.
            mkind = articleObj.getString("kind");
            mtrackName = articleObj.getString("trackName");
            martistName = articleObj.getString("artistName");
            mcollectionName = articleObj.getString("collectionName");
            mtrackViewUrl = articleObj.getString("trackViewUrl");
            mpreviewUrl = articleObj.getString("previewUrl");
            martworkUrl60 = articleObj.getString("artworkUrl60");

            // There may or may not be a body text snippet in the response, so check before trying
            // to use it.
            //if (articleObj.has("artworkUrl60")) {
              //  martworkUrl60 = articleObj.getString("artworkworkUrl60");
            //}


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getKind() { return mkind; }

    public String getTrackName() {
        return mtrackName;
    }

    public String getArtistName() {
        return martistName;
    }

    public String getCollectionName() {
        return mcollectionName;
    }

    public String getPreviewUrl() { return mpreviewUrl; }

    public String getArtworkUrl60() {
        return martworkUrl60;
    }

    public String getTrackViewUrl() {
        return mtrackViewUrl;
    }

}

