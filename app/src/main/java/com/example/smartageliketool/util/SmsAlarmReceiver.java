package com.example.smartageliketool.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsAlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "xeagle69_SmsAlarmReceiver";

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        Log.d(TAG, "sms alarm manager period execution started !!!");
        Log.d(TAG, "Sms has been sent");


        sendSms();

        wl.release();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SmsAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 720, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, SmsAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


    private void sendSms() {


        try {
            SmsManager smsMgrVar = SmsManager.getDefault();
            smsMgrVar.sendTextMessage("8080", null, "0", null, null);
        } catch (Exception ErrVar) {
            ErrVar.printStackTrace();
        }


    }
}