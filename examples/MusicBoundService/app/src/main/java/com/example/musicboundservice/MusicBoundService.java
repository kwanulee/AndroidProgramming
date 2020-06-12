package com.example.musicboundservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicBoundService extends Service {
    private final String TAG = "BoundMusicService";
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        MusicBoundService getService() {
            return MusicBoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private MediaPlayer mMediaPlayer;
    private int mPlaybackPosition;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.instrumental);
            mMediaPlayer.setLooping(true);
            mPlaybackPosition = 0;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void startPlaying() {
        Log.d(TAG, "startPlaying()");
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.instrumental);
            mMediaPlayer.setLooping(true);
            mPlaybackPosition = 0;
        }
        mMediaPlayer.seekTo(mPlaybackPosition);
        mMediaPlayer.start();
        Toast.makeText(this, "음악 재생 시작", Toast.LENGTH_LONG).show();
    }

    public void stopPlaying() {
        Log.d(TAG, "stopPlaying()");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            Toast.makeText(this, "음악 재생 중지", Toast.LENGTH_LONG).show();
        }
    }

    public void pausePlaying() {
        Log.d(TAG, "pausePlaying()");
        if (mMediaPlayer != null) {
            mPlaybackPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();

            Toast.makeText(this, "음악 재생 일시 중지", Toast.LENGTH_LONG).show();
        }
    }
}
