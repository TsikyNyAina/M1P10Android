package com.example.disign;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.RequiresApi;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.Map;

public class SocketManager {
    private static Socket socket;
    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showNotification(Context context, NotificationModel notif) {

        Spanned formattedMessage;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            formattedMessage = Html.fromHtml(notif.getContent(), Html.FROM_HTML_MODE_LEGACY);
        } else {
            formattedMessage = Html.fromHtml(notif.getContent());
        }



        Notification.Builder builder = new Notification.Builder(context, "YOUR_CHANNEL_ID")
                .setContentTitle(notif.getTitle())
                .setContentText(formattedMessage)
                .setSmallIcon(R.drawable.ic_icon_tourisme); // Replace with your notification icon

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




    public static Socket getSocket() {
        if (socket == null) {
            try {
                IO.Options opts = new IO.Options();
                opts.forceNew = true; // Depending on your server configuration, you may or may not need this line
                socket = IO.socket("https://m1p10androidnode.onrender.com", opts);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }
    public static void disconnectSocket() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
        }
    }
}

