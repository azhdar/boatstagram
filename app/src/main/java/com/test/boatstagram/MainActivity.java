package com.test.boatstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnBoatListListener {

    public final static String EXTRA_BOATSTAGRAM_BIG_IMAGE_URL = "com.test.boatstagram.BOATSTAGRAM_BIG_IMAGE_URL";
    public final static String LOG_TAG = "BoatList";

    private BoatStagramListAdapter boatStagramListAdapter;
    private ListView listView;
    private FloatingActionButton floatingRefreshButton;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingRefreshButton = (FloatingActionButton) findViewById(R.id.fab);
        if (floatingRefreshButton != null) {
            floatingRefreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingRefreshButton.setEnabled(false);
                    boatStagramListAdapter.notifyDataSetChanged();
                    BoatList.getInstance().updateBoatStagramFromApi();
                    setDownloadAllImagesButtonEnabled(true);
                }
            });
        }

        listView = (ListView) findViewById(R.id.listView);

        listView.setEmptyView(findViewById(R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoatStagramItem boatStagramItem = BoatList.getInstance().getBoatStagramItem(position);
                Intent intent = new Intent(getBaseContext(), FullscreenBoatStagramItemActivity.class);
                intent.putExtra(EXTRA_BOATSTAGRAM_BIG_IMAGE_URL, boatStagramItem.getDisplayImageUrl());
                startActivity(intent);
            }
        });

        BoatList.getInstance().addOnBoatListListener(this);

        boatStagramListAdapter = new BoatStagramListAdapter(this, BoatList.getInstance().getBoatStagramItemArrayList());

        listView.setAdapter(boatStagramListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setDownloadAllImagesButtonEnabled(true);
    }

    @Override
    public void onSuccess(ArrayList<BoatStagramItem> boatStagramItemArrayList) {
        boatStagramListAdapter.notifyDataSetChanged();
        floatingRefreshButton.setEnabled(true);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Can't load boats. Try again.", Toast.LENGTH_LONG).show();
        floatingRefreshButton.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_download_all_images) {
            setDownloadAllImagesButtonEnabled(false);

            Intent downloadAllImagesStickyService = new Intent(this, DownloadAllImagesStickyService.class);
            startService(downloadAllImagesStickyService);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDownloadAllImagesButtonEnabled(boolean enabled) {
        if (menu == null || menu.getItem(0) == null)
            return;

        menu.getItem(0).setEnabled(enabled);
    }

    private void updateDownloadAllImagesButtonText() {
        if (menu == null || menu.getItem(0) == null)
            return;

        //menu.getItem(0).setEnabled(BoatList.getInstance().);
    }

}
