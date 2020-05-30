package com.example.kwanwoo.multimediatest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class MediaItem {
    final static int RAW=0;
    final static int SDCARD=1;
    final static int WEB=2;
    final static int GALLERY=6;
    final static int AUDIO=3;
    final static int VIDEO=4;
    final static int IMAGE=5;

    int source;
    String name;
//    Uri uri;
    int type;
   // Bitmap bitmap;

    public MediaItem(int src, String name, int type) {
        this.source = src;
        this.name = name;
        this.type = type;
    }

    /*
    public MediaItem(int src, String name, Uri uri) {
        source = src;
        this.name = name;
        this.uri = uri;
        type = AUDIO;
    }

    public MediaItem(int src, String name, Uri uri, int type) {
        source = src;
        this.name = name;
        this.uri = uri;
        type = AUDIO;
//        if (type == IMAGE) {
//            bitmap = BitmapFactory.decodeFile(uri.getPath());
//            Log.i("MediaItem","uri.getPath()="+uri.getPath());
//        }
//        else bitmap = null;
    }

     */
}
