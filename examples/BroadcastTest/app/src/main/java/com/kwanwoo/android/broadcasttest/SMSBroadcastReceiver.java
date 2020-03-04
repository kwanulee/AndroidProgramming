package com.kwanwoo.android.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            if (messages != null) {
                if (messages.length == 0)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < messages.length; i++) {
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();

                Log.i("SMSBroadcastReceiver","From " + sender + " : "+message);
                Toast.makeText(context, "From" + sender + " : "+message, Toast.LENGTH_SHORT).show();


            }
        }
    }
}
