package com.test.boatstagram;

import org.json.JSONException;
import org.json.JSONObject;

public class BoatStagramItem {

    private long id;
    private String caption;
    private String thumbnailUrl;
    private String displayImageUrl;
    private int height;

    public BoatStagramItem(JSONObject jsonObject) throws JSONException {
        parseJson(jsonObject);
    }

    private void parseJson(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getLong("id");
        caption = jsonObject.getString("caption");
        thumbnailUrl = jsonObject.getString("thumbnail_src");
        displayImageUrl = jsonObject.getString("display_src");
        height = jsonObject.getJSONObject("dimensions").getInt("height");
    }

    public long getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDisplayImageUrl() {
        return displayImageUrl;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return getHeight() + " - " + getCaption() + " (" + getThumbnailUrl() + " - " + getDisplayImageUrl() + ")";
    }
}
