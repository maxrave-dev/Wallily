package com.maxrave.wallily.service;


import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class DownloadManager implements Downloader {
    private final Context context;

    private final android.app.DownloadManager downloadManager;
    public DownloadManager(Context context) {
        this.context = context;
        downloadManager = this.context.getSystemService(android.app.DownloadManager.class);
    }
    @Override
    public Long download(String url) {
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url))
                .setMimeType("image/jpeg")
                .setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI | android.app.DownloadManager.Request.NETWORK_MOBILE)
                .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle("Download Image")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Wallily/" + System.currentTimeMillis() + ".jpg")
                ;
        return downloadManager.enqueue(request);
    }
}
