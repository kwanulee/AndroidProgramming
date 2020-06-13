package com.example.sensorreading;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class SensorReadingService extends Service implements SensorEventListener {
    final static String READING = "com.example.sensorreading.SensorReadingService.READING";
    private String reading;
    private SensorManager mgr;
    private List<Sensor> sensorList;
    static final String TAG="SensorReading";
    Intent intent = new Intent(READING);
    private float mX;
    private float mY;
    private float mZ;

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        mgr = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorList = mgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
        for (Sensor sensor: sensorList) {
            mgr.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        mgr.unregisterListener(this);
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        SensorReadingService getService() {
            return SensorReadingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getZ() {
        return mZ;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG,"onSensorChanged");

        mX = event.values[0];
        mY = event.values[1];
        mZ = event.values[2];

        intent.putExtra("mX", mX);
        intent.putExtra("mY", mY);
        intent.putExtra("mZ", mZ);
        intent.putExtra("reading",""+mX);
        sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
