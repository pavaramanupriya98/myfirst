package com.moraspirit.moraspiritapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.article.Article;
import com.moraspirit.moraspiritapp.article.ArticleViewFragment;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID_ARTICLE = "article";
    private static final String CHANNEL_ID_SPORT = "sport";
    private static final String CHANNEL_ID_OTHER = "other";

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";

    String ID ;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotificationChannel(getApplicationContext());

        if(remoteMessage.getNotification().getChannelId() == null) ID =   remoteMessage.getData().get("id");
        else ID = remoteMessage.getNotification().getChannelId();
        System.out.println(remoteMessage.getData().get("id"));
        ID = checkChannel(ID)?ID: CHANNEL_ID_OTHER;

        System.out.println(ID);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(this, ID)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        if(ID.equals(CHANNEL_ID_ARTICLE)) {
            Intent resultIntent = new Intent(this, ArticleViewFragment.class);
            ArticleViewFragment.setArticle(getArticle(remoteMessage.getData()));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    private boolean checkChannel(String channel){
        if(channel == null)return false;
        if(channel.equals(CHANNEL_ID_ARTICLE) || channel.equals(CHANNEL_ID_SPORT)) return true;
        return false;
    }
    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name_1);
            String description = context.getString(R.string.channel_description_1);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_ARTICLE, name, importance);
            channel.setDescription(description);

            CharSequence name2 = context.getString(R.string.channel_name_2);
            String description2 = context.getString(R.string.channel_description_2);
            int importance2 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID_SPORT, name2, importance2);
            channel2.setDescription(description2);

            CharSequence name3 = context.getString(R.string.channel_name_3);
            String description3 = context.getString(R.string.channel_description_3);
            int importance3 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel3 = new NotificationChannel(CHANNEL_ID_OTHER, name3, importance3);
            channel3.setDescription(description3);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel2);
            notificationManager.createNotificationChannel(channel3);

        }
    }

    public static Article getArticle(Map<String,String> data){
        Article article = new Article();
        article.setAuthor(data.get("author"));
        article.setCategory(data.get("category"));
        article.setContent(data.get("content"));
        article.setDate(data.get("date"));
        article.setEditor(data.get("editor"));
        article.setKeywords(data.get("keywords"));
        article.setImage(data.get("image"));
        article.setLanguage(data.get("language"));
        article.setSlug(data.get("slug"));
        article.setMeta(data.get("meta"));
        article.setTitle(data.get("title"));
        article.setPosition(data.get("position"));

        return article;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        String token = FirebaseInstanceId.getInstance().getToken();

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
//        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }
}
