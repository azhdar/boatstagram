package com.test.boatstagram;

import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;

public class DownloadAllImagesStickyService extends StickyService {

    private String BOATSTAGRAM_PUBLIC_PICTURES_DIRECTORY_NAME = "BoatStagram";

    private File boatStagramAlbumStorageDirectoryPath;

    @Override
    protected void onStickyCreate() {
        boatStagramAlbumStorageDirectoryPath = getBoatStagramAlbumStorageDirectoryPath();
    }

    @Override
    protected void onStickyStartCommand(Intent intent, int flags, int startId) {
        if (BoatList.getInstance().getBoatStagramItemArrayList().size() == 0) {
            Toast.makeText(this, "Nothing to download", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, BoatList.getInstance().getBoatStagramItemArrayList().size() + " image(s) will be downloaded", Toast.LENGTH_LONG).show();
            downloadImages();
        }
    }

    @Override
    protected int getNotificationId() {
        return 123456;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     *  PRIVATE METHODS
     */

    private void downloadImages() {
        AsyncHttpClient client = new AsyncHttpClient();

        for (final BoatStagramItem item : BoatList.getInstance().getBoatStagramItemArrayList()) {
            client.get(item.getDisplayImageUrl(), new FileAsyncHttpResponseHandler(this) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    if (statusCode == 200) {
                        try {
                            FileInputStream tmpFileInputStream = new FileInputStream(file);
                            File imageFile = getFileWithUniqueFileName(boatStagramAlbumStorageDirectoryPath, String.valueOf(item.getId()), ".jpg");
                            FileOutputStream imageFileOutputStream = new FileOutputStream(imageFile);
                            readFromStreamAndWriteItToStream(tmpFileInputStream, imageFileOutputStream, true, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    Log.e(MainActivity.LOG_TAG, "Can't download an image");
                }
            });
        }
    }

    private File getBoatStagramAlbumStorageDirectoryPath() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), BOATSTAGRAM_PUBLIC_PICTURES_DIRECTORY_NAME);
        if (!file.mkdirs()) {
            Log.e(MainActivity.LOG_TAG, "Directory not created");
        }
        return file;
    }

    private int readFromStreamAndWriteItToStream(InputStream inputStream, OutputStream outputStream, boolean closeInputStream, boolean closeOutputStream) throws IOException {
        if (inputStream == null || outputStream == null)
            return -1;
        int readByte = 0;
        int totalReadByte = 0;
        byte[] buffer = new byte[1024];
        while ((readByte = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readByte);
            totalReadByte += readByte;
        }
        if (closeInputStream)
            inputStream.close();
        if (closeOutputStream)
            outputStream.close();
        return totalReadByte;
    }

    private File getFileWithUniqueFileName(File directory, String fileNameWithoutExtensionAndDot, String extensionWithDot) {
        if (!directory.isDirectory() && directory.exists())
            return null;
        if (fileNameWithoutExtensionAndDot == null)
            fileNameWithoutExtensionAndDot = "";
        if (extensionWithDot == null)
            extensionWithDot = "";

        File file = null;
        if (!fileNameWithoutExtensionAndDot.equals("") && !extensionWithDot.equals("")) {
            file = new File(directory, fileNameWithoutExtensionAndDot + extensionWithDot);
            if (!file.exists())
                return file;
        }

        int i = 1;
        while ((file = new File(directory, fileNameWithoutExtensionAndDot + " (" + i + ")" + extensionWithDot)).exists())
            i++;
        return file;
    }
}
