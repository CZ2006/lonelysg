package com.IrisBICS.lonelysg.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.IrisBICS.lonelysg.Fragments.ManageActivitiesUI;
import com.IrisBICS.lonelysg.Fragments.AccountUI;
import com.IrisBICS.lonelysg.Fragments.ChatUI;
import com.IrisBICS.lonelysg.Fragments.DiscoveryPageUI;
import com.IrisBICS.lonelysg.R;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

public class NavigationBarUI extends AppCompatActivity {
    MeowBottomNavigation meo;
    private final static int ID_DISCOVERY = 1;
    private final static int ID_INVITATION = 2;
    private final static int ID_CHAT = 3;
    private final static int ID_ACCOUNT = 4;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meo = (MeowBottomNavigation) findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1, R.drawable.search_black));
        meo.add(new MeowBottomNavigation.Model(2, R.drawable.add_button));
        meo.add(new MeowBottomNavigation.Model(3, R.drawable.chat_black));
        meo.add(new MeowBottomNavigation.Model(4, R.drawable.account_circle));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountUI()).commit();

        meo.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });

        meo.setOnShowListener(new MeowBottomNavigation.ShowListener(){
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment select_fragment = null;
                switch(item.getId()){
                    case ID_ACCOUNT:
                        select_fragment = new AccountUI();
                        break;
                    case ID_CHAT:
                        select_fragment = new ChatUI();
                        break;
                    case ID_DISCOVERY:
                        select_fragment = new DiscoveryPageUI();
                        break;
                    case ID_INVITATION:
                        select_fragment = new ManageActivitiesUI();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment).commit();
            }
        });

        PushNotifications.start(getApplicationContext(), "211e38a9-4bc8-40c5-958a-4a7f9aa91547");
        PushNotifications.addDeviceInterest("debug-apple");
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                String messagePayload = remoteMessage.getData().get("inAppNotificationMessage");
                if (messagePayload == null) {
                    // Message payload was not set for this notification
                    Log.i("MyActivity", "Payload was missing");
                } else {
                    Log.i("MyActivity", messagePayload);
                    Toast.makeText(NavigationBarUI.this, "You received a request", Toast.LENGTH_SHORT).show();
                    // Now update the UI based on your message payload!
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
