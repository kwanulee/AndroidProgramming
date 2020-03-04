package com.kwanwoo.android.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryWatchBR extends BroadcastReceiver {
    TextView mStatus;

    public BatteryWatchBR(TextView status) {
        mStatus = status;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        printBatteryStatus(batteryStatus);
    }

    private void printBatteryStatus(Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
        int plug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);

        String sPlug, sStatus, sHealth;

        switch(plug) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                sPlug = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                sPlug = "USB";
                break;
            default:
                sPlug = "Battery";
                break;
        }

        switch(status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                sStatus = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                sStatus = "Not Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                sStatus = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                sStatus = "Full";
                break;
            default:
                sStatus ="UnKnown";
        }

        switch(health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                sHealth = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                sHealth = "Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                sHealth = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                sHealth = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                sHealth = "Overheat";
                break;

            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                sHealth = "Failed";
                break;
            default:
                sHealth = "Unknown";
        }

        mStatus.setText(String.format("Remain: %d \nConnection: %s\n Health: %s \n Status: %s\n ", level, sPlug, sHealth, sStatus));
    }
}
