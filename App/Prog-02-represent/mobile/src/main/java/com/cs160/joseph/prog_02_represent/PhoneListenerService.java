package com.cs160.joseph.prog_02_represent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by JOSEPH on 3/3/16.
 */
public class PhoneListenerService extends WearableListenerService {
    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST = "/send_toast";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        String data = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        if (data.length() == 5) {
            Log.d("RECEIVED ZIPCODE: ", data);
            Intent intent = new Intent(this, CongressionalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Need to add this flag since you're starting a new activity from a service
            intent.putExtra("REPS_NAMES", new String[]{"Person D", "Person E", "Person F"});
            intent.putExtra("REPS_PARTIES", new String[]{"Democrat", "Democrat", "Republican"});
            intent.putExtra("REPS_EMAILS", new String[]{"D@gmail.com", "E@gmail.com", "F@gmail.com"});
            intent.putExtra("REPS_WEBSITES", new String[]{"www.D.com", "www.E.com", "www.F.com"});
            intent.putExtra("REPS_TWEETS", new String[]{"This is tweet D", "This is tweet E", "This is tweet F"});
            intent.putExtra("REPS_TITLES", new String[]{"Representative D", "Representative E", "Representative F"});

            Intent watchIntent = new Intent(this, PhoneToWatchService.class);
            String watchData = TextUtils.join(",", new String[]{"Person D", "Person E", "Person F"});
            watchData += ";" + TextUtils.join(",", new String[]{"Representative D", "Representative E", "Representative F"});
            watchIntent.putExtra("DATA", watchData);

            startActivity(intent);
            startService(watchIntent);
        } else {
            Log.d("RECEIVED DATA:", data);
            String[] dataArray = data.split(";");
            Intent intent = new Intent(this, DetailedActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Need to add this flag since you're starting a new activity from a service
            intent.putExtra("BIOGUIDE_ID", data);
            Log.d("T", "about to start phone DetailedActivity with DATA: " + data);
            startActivity(intent);
        }
    }
}
