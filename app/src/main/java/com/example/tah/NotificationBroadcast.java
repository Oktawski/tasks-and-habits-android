package com.example.tah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(context, "task")
                .setSmallIcon(R.drawable.ic_add_24px)
                .setContentTitle("Tasks waiting")
                .setContentText("You have not completed tasks!!!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat
                = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200, builder.build());
    }
}
