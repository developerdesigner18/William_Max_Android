package com.afrobiz.afrobizfind.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.category.CategoryDetailActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "MyMessagingService";

    String msgId = null, title;
    String last50 = null;
    String company_id = null;
    NotificationManager notificationManager;

    @Override
    public void onNewToken(String token)
    {
        super.onNewToken(token);
        Log.e("newToken", token);

        new PrefrenceManager(this).set_FcmToken(token);

        String fcmtoken1 = new PrefrenceManager(this).get_FcmToken();

        Log.e("fcm_token1", "" + fcmtoken1);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0)
        {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            company_id = remoteMessage.getData().get("company_id");
            last50 = remoteMessage.getData().get("last50");
            Log.e("com_id",""+company_id);
            Log.e("last50Msg",""+last50);
        }
        title = remoteMessage.getNotification().getTitle();
        Log.e("title",""+title);
        String click_action = remoteMessage.getNotification().getClickAction();
        if (remoteMessage.getNotification() != null)
        {
        }
        sendNotiication(title, last50, click_action, company_id);

    }
    private void sendNotiication( String title, String last50, String click_action,String company_id)
    {
        Log.e("action", "" + click_action);
        Intent intent = new Intent(this , CategoryDetailActivity.class);
        intent.putExtra("categoryid" , 0);
        intent.putExtra("title", title);
        intent.putExtra("last50", last50);
        intent.putExtra("company_id", company_id);
        Log.e("last50nioti", "" + last50);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //For Android Version oreo and greater than oreo.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel mChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), importance);

            mChannel.setImportance(importance);
            mChannel.setShowBadge(false);
            mChannel.setSound(null, null);
            mChannel.enableLights(false);
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{0});

            mNotifyManager.createNotificationChannel(mChannel);
        }
        //For Android Version lower than oreo.
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id));
        mBuilder.setContentTitle(title)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setSound(null)
                .setColor(getResources().getColor(R.color.grey_dark))
                .setContentIntent(pendingIntent)
                .setChannelId(getString(R.string.default_notification_channel_id))
                .setPriority(NotificationCompat.PRIORITY_LOW);
        assert mNotifyManager != null;
        mNotifyManager.notify(123, mBuilder.build());
    }
}

