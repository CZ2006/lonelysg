package com.IrisBICS.lonelysg.Utils;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;

public class PusherMessagingService extends MessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("PusherMessagingService", "Got a remote message 🎉");
        System.out.println("phase5");
        Toast.makeText(PusherMessagingService.this,"You received a request", Toast.LENGTH_SHORT).show();
    }
}
