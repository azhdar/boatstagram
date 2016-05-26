package com.test.boatstagram;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BoatList {

    private static BoatList mInstance = null;
    private final String DATA_URL = "https://www.instagram.com/explore/tags/boat/?__a=1";
    private ArrayList<BoatStagramItem> boatStagramItemArrayList;
    private ArrayList<OnBoatListListener> onBoatListListeners;

    public BoatList() {
        boatStagramItemArrayList = new ArrayList<BoatStagramItem>();

        onBoatListListeners = new ArrayList<OnBoatListListener>();

        updateBoatStagramFromApi();
    }

    public static BoatList getInstance() {
        if (mInstance == null) {
            mInstance = new BoatList();
        }
        return mInstance;
    }

    public ArrayList<BoatStagramItem> getBoatStagramItemArrayList() {
        return boatStagramItemArrayList; //TODO return a copy
    }

    public BoatStagramItem getBoatStagramItem(int position) {
        return boatStagramItemArrayList.get(position);
    }

    public void updateBoatStagramFromApi() {

        boatStagramItemArrayList.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(DATA_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                // boatStagramItemArrayList.clear(); //TODO ??

                // We parse the JSONObject response
                try {
                    JSONArray dataArray = response.getJSONObject("tag").getJSONObject("media").getJSONArray("nodes");

                    for (int i = 0; i < dataArray.length(); i++) {
                        boatStagramItemArrayList.add(new BoatStagramItem(dataArray.getJSONObject(i)));
                    }

                } catch (Exception e) {
                    Log.e(MainActivity.LOG_TAG, "Error while parsing");
                }

                Log.d(MainActivity.LOG_TAG, "boatStagramItemArrayList size = " + boatStagramItemArrayList.size());

                fireOnSuccess(boatStagramItemArrayList);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(MainActivity.LOG_TAG, "Bad API response");
                fireOnError();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(MainActivity.LOG_TAG, "Error " + statusCode);
                Log.e(MainActivity.LOG_TAG, responseString, throwable);
                fireOnError();
            }

        });
    }

    /*public String[] getUrlPhotosStringArray() {
        if (boatStagramItemArrayList == null)
            return new String[0];

        ArrayList<String> urlPhotosList = new ArrayList<>();
        for (BoatStagramItem item : boatStagramItemArrayList) {
            urlPhotosList.add(item.getDisplayImageUrl());
        }
        return urlPhotosList.toArray(new String[boatStagramItemArrayList.size()]);
    }*/

    /*
        LISTENERS
     */

    public void addOnBoatListListener(OnBoatListListener onBoatListListener) {
        if (this.onBoatListListeners != null && onBoatListListener != null) {
            this.onBoatListListeners.add(onBoatListListener);
        }
    }

    public void removeOnBoatListListeners() {
        if (this.onBoatListListeners != null) {
            while (!this.onBoatListListeners.isEmpty()) {
                this.onBoatListListeners.remove(0);
            }
        }
    }

    public void fireOnSuccess(ArrayList<BoatStagramItem> boatStagramItemArrayList) {
        if (this.onBoatListListeners != null) {
            for (int i = 0; i < this.onBoatListListeners.size(); i++)
                this.onBoatListListeners.get(i).onSuccess(boatStagramItemArrayList);
        }
    }

    public void fireOnError() {
        if (this.onBoatListListeners != null) {
            for (int i = 0; i < this.onBoatListListeners.size(); i++)
                this.onBoatListListeners.get(i).onError();
        }
    }
}
