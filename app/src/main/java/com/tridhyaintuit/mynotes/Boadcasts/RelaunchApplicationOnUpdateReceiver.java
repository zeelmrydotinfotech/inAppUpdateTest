package com.tridhyaintuit.mynotes.Boadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;

public class RelaunchApplicationOnUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            ApplicationInfo app = new ApplicationInfo();
//            if (app.packageName.equals("it.android.downloadapk")) {
                Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(app.packageName);
                context.startActivity(LaunchIntent);
                Toast.makeText(context, "Your app is updated to new version, Please open again", Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
