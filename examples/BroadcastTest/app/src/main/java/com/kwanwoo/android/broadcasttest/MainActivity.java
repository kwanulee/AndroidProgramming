package com.kwanwoo.android.broadcasttest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver mSystemBR;
    BroadcastReceiver mSMSBR;
    BroadcastReceiver mBatteryBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSystemBR = registSystemBR();

        if (checkPermission()) {
            mSMSBR = registSMSBR();
        } else
            requestPermission();
    }


    public void onResume() {
        super.onResume();

        TextView status_output = findViewById(R.id.status);

        mBatteryBR = new BatteryWatchBR(status_output);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mBatteryBR, filter);

    }

    public void onPause() {
        super.onPause();

        unregisterReceiver(mBatteryBR);
    }

    private BroadcastReceiver registSystemBR() {
        SystemBroadcastReceiver my_br = new SystemBroadcastReceiver();
        IntentFilter intentFilter_br = new IntentFilter();
        intentFilter_br.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter_br.addAction(Intent.ACTION_BOOT_COMPLETED);
        //intentFilter_br.addAction(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(my_br, intentFilter_br);

        return my_br;
    }

    private BroadcastReceiver registSMSBR() {
        SMSBroadcastReceiver sms_br = new SMSBroadcastReceiver();
        IntentFilter intentFilter_sms = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(sms_br, intentFilter_sms);

        return sms_br;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mSystemBR);
        if (mSMSBR != null)
            unregisterReceiver(mSMSBR);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_FOR_RECEIVE_SMS) {
            if (grantResults.length >0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mSMSBR = registSMSBR();
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;
    }

    final int REQUEST_PERMISSION_FOR_RECEIVE_SMS = 1;
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_PERMISSION_FOR_RECEIVE_SMS);
    }
}
