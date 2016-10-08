package com.example.nocomment.snapchat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Luna on 17/09/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    //onMessageReceived() get called if a new message is received from Firebase Cloud Message
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        switch (remoteMessage.getData().get("type")){
            case "fridendshipRequest":
                fridendshipRequest(remoteMessage.getData().get("user"),remoteMessage.getData().get("message"));

        }
       // showNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("message"));
    }
    private void fridendshipRequest(String userid,String message){
        Intent intent = new Intent(this, FriendRequest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userid",userid);
        intent.putExtra("message",message);
        startActivity(intent);




    }
    //  show a notification for received message
    private void showNotification(String name, String remoteMessage){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",remoteMessage);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My FCM application")
                .setContentText(name+":"+remoteMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        System.out.println("@ "+remoteMessage);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build());

        Intent intentRece = new Intent(REGISTRATION_SUCCESS);
        intentRece.putExtra("message", remoteMessage);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentRece);
    }
}
