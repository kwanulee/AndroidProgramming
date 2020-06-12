package com.example.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {
    private final String TAG="MusicService";
    private MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.instrumental);
            mMediaPlayer.setLooping(true);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy()");
        if (mMediaPlayer !=null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand()");
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            Toast.makeText(this,"음악 재생 시작",Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }
}
