package com.IrisBICS.lonelysg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class LoginPage extends AppCompatActivity {
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new com.IrisBICS.lonelysg.FragmentAccount()).commit();

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
                        select_fragment = new com.IrisBICS.lonelysg.FragmentAccount();
                        break;
                    case ID_CHAT:
                        select_fragment = new com.IrisBICS.lonelysg.FragmentChat();
                        break;
                    case ID_DISCOVERY:
                        select_fragment = new com.IrisBICS.lonelysg.FragmentDiscovery();
                        break;
                    case ID_INVITATION:
                        select_fragment = new com.IrisBICS.lonelysg.FragmentInvitation();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment).commit();
            }
        });
    }
}
