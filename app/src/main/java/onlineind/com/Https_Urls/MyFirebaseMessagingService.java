package onlineind.com.Https_Urls;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import onlineind.com.Activity_Package.MainActivity;
import onlineind.com.R;
/**
 * Created by google on 21/9/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {

    //String message= (Html.fromHtml(remoteMessage.getData().get("message")));
  String title= String.valueOf(Html.fromHtml(remoteMessage.getData().get("message")));

    Intent intent=new Intent(this,MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder nb=new NotificationCompat.Builder(this);

    nb.setContentTitle("Online India");
    nb.setContentText( Html.fromHtml(remoteMessage.getData().get("message")));
    nb.setAutoCancel(true);
    nb.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
    nb.setStyle(new NotificationCompat.BigTextStyle()
            .bigText(Html.fromHtml(remoteMessage.getData().get("message"))));
    //LED
    nb.setLights(Color.RED, 3000, 3000);

    nb.setContentIntent(pendingIntent);
    nb.setSmallIcon(R.drawable.notify_24);
    NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(0,nb.build());

  }
}
