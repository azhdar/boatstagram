package com.test.boatstagram.service;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.test.boatstagram.data.BoatList;
import com.test.boatstagram.data.BoatStagramItem;
import com.test.boatstagram.utils.FileUtils;

import java.util.ArrayList;

public class DownloadService extends Service {

    private static int NOTIFICATION = 666;

    private BroadcastReceiver broadcastReceiver;
    private ArrayList<Long> downloadingIds;

    //TODO stress test in background without BoatStagram opened and with more files

    public DownloadService() {

    }

    @Override
    public void onCreate() {
        downloadingIds = new ArrayList<>();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    if (downloadingIds.contains(downloadId)) {
                        if (downloadingIds.remove(downloadId)) {
                            if (downloadingIds.isEmpty()) {
                                Intent downloadAppIntent = new Intent();
                                downloadAppIntent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                                PendingIntent pendingIntent = PendingIntent.getActivity(
                                        context,
                                        0,
                                        downloadAppIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(context)
                                                .setSmallIcon(android.R.drawable.ic_menu_gallery)
                                                .setContentTitle("BoatStagram")
                                                .setContentText("All images downloaded!")
                                                .setAutoCancel(true)
                                                .setContentIntent(pendingIntent);
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(NOTIFICATION, mBuilder.build());

                            }
                        } else {
                            Log.e("BoatStagram", "Can't remove " + downloadId);
                        }
                    } else {
                        Log.e("BoatStagram", "Can't find " + downloadId);
                    }

                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        for (final BoatStagramItem boatStagramItem : BoatList.getInstance().getBoatStagramItemArrayList()) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(boatStagramItem.getDisplayImageUrl()));
            request.setDescription(boatStagramItem.getCaption());
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

            FileUtils.getFileWithUniqueFileName(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), String.valueOf(boatStagramItem.getId()), ".jpg");

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(boatStagramItem.getId()) + ".jpg");

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadingIds.add(manager.enqueue(request));
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
