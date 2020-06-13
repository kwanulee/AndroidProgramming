package com.example.sensorreading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    MyReceiver myReceiver = null;
    Intent intent;
    static final String TAG="SensorReading";
    TextView textfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textfield = findViewById(R.id.editText);

        intent = new Intent(this, SensorReadingService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG,"onCreate/bindService");

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView output = findViewById(R.id.sensorResult);
                if (mService != null) {
                    String result = String.format("X= %f, Y=%f, Z=%f", mService.getX(),
                            mService.getY(), mService.getZ());
                    output.setText(result);
                }
            }
        });
    }

    SensorReadingService mService;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SensorReadingService.LocalBinder binder = (SensorReadingService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume/registering receiver");
        myReceiver = new MyReceiver(textfield);

        IntentFilter filter = new IntentFilter();
        filter.addAction(SensorReadingService.READING);
        registerReceiver(myReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause/unregistering receiver");

        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop/stopService");
        stopService(intent);
    }
}
