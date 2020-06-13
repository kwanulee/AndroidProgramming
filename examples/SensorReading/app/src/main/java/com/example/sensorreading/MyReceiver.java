package com.example.sensorreading;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class MyReceiver extends BroadcastReceiver {
    static final String TAG="SensorReading";
    TextView output;

    MyReceiver(TextView output) {
        this.output = output;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int x = intent.getIntExtra("mX",0);
        int y = intent.getIntExtra("mY",0);
        int z = intent.getIntExtra("mZ",0);
        String s = intent.getStringExtra("reading");
        Log.d(TAG,"onReceive:s="+s);
        output.setText(
                String.format("x=%d, y=%d, z=%d",x,y,z)
        );
    }
}
