package com.tridhyaintuit.mynotes.Boadcasts;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.tridhyaintuit.mynotes.Activities.AddNote;
import com.tridhyaintuit.mynotes.R;

public class NotificationReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "Reminder";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent actionIntent = new Intent(context, AddNote.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent toastIntent = new Intent(context, ReceiverToast.class);
        toastIntent.putExtra("toast", "This is a notification Message");
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification.Action actionToast = new Notification.Action.Builder(R.mipmap.ic_launcher,
                "Toast Message", toastPendingIntent).build();


        Intent dismissIntent = new Intent(context, ReceiverDismiss.class);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification.Action actionDismiss = new Notification.Action.Builder(R.mipmap.ic_launcher,
                "Dismiss", dismissPendingIntent).build();


        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.android);
            String text = "This is notification testing texts for scrollable texts after notification received";

            Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.reminder)
                    .setContentTitle("Reminder")
                    .setContentText("Your reminder For Note")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setColor(Color.BLUE)
                    .addAction(actionToast)
                    .addAction(actionDismiss)
                    .setLargeIcon(image)
                    .setStyle(new Notification.BigPictureStyle().bigPicture(image));
//                    .setStyle(new Notification.BigTextStyle().bigText(text));

            NotificationManagerCompat compat = NotificationManagerCompat.from(context);
            compat.notify(1, builder.build());
        }
    }
}
