package com.example.wlgusdn.myapplication;


import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.example.wlgusdn.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressWarnings("ALL")
public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private final static String TAG = "asdasdasd";

    private FirebaseDatabase mFirebaseDatabase;

    private UserData1 user;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        mFirebaseDatabase = FirebaseDatabase.getInstance();




            if (remoteMessage.getNotification() != null) {
                String body = remoteMessage.getNotification().getBody();
                Log.d(TAG, "Notification Body: " + body);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher) // 알림 영역에 노출 될 아이콘.
                        .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                        .setContentText(body); // Firebase Console 에서 사용자가 전달한 메시지내용


                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(0x1001, notificationBuilder.build());
            }

    }







    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("asdasdasd",s);



    }


}
