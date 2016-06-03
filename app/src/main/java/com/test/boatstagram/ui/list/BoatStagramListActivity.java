package com.test.boatstagram.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.test.boatstagram.R;
import com.test.boatstagram.data.BoatList;
import com.test.boatstagram.data.BoatStagramItem;
import com.test.boatstagram.data.OnBoatListListener;
import com.test.boatstagram.service.DownloadService;
import com.test.boatstagram.ui.fullscreen.FullscreenBoatStagramItemActivity;

import java.util.ArrayList;

public class BoatStagramListActivity extends AppCompatActivity implements OnBoatListListener {

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


        /*BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            view.setImageURI(Uri.parse(uriString));
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));*/
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

            if (BoatList.getInstance().getBoatStagramItemArrayList().size() == 0) {
                Toast.makeText(this, "Nothing to download", Toast.LENGTH_LONG).show();
                return true;
            } else {
                Toast.makeText(this, BoatList.getInstance().getBoatStagramItemArrayList().size() + " image(s) will be downloaded", Toast.LENGTH_LONG).show();
                Intent downloadServiceIntent = new Intent(this, DownloadService.class);
                startService(downloadServiceIntent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDownloadAllImagesButtonEnabled(boolean enabled) {
        if (menu == null || menu.getItem(0) == null)
            return;

        menu.getItem(0).setEnabled(enabled);
    }

}
