package com.example.musicboundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //4. 서비스 객체의 공개 메소드 사용
                mService.startPlaying();
            }
        });

        Button pause = findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.pausePlaying();
            }
        });

        Button stop = findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.stopPlaying();
            }
        });

        Button move = findViewById(R.id.next);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AnotherAvtivity.class));
            }
        });
    }

    MusicBoundService mService;
    boolean mBound = false;

    // 1. 서비스와의 연결 상태를 모니터링하는 ServiceConnection 구현 객체 생성
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBoundService.LocalBinder binder = (MusicBoundService.LocalBinder)service;

            // 3. 클라이언트는 바인딩된 서비스 객체 인스턴스를 얻습니다.
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 2. 클라이언트는 bindService() 를 호출하여 서비스에 바인딩합니다.
        bindService(new Intent(MainActivity.this, MusicBoundService.class),
                mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(mConnection);
        super.onStop();
    }
}
