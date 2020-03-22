package com.IrisBICS.lonelysg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;

public class InvitationsUI extends AppCompatActivity {

    ListView invitationsList;
    String[] activityName = {"Example 1", "Example 2", "Example 3", "Example 4","Example 5"};
    String[] activityDateTime = {"12pm - 1pm, 1 April 2020", "2pm - 4pm, 5 April 2020", "11am - 1pm, 11 April 2020", "10pm - 10.30pm, 15 April 2020", "10am - 11am, 19 April 2020"};
    int userImage[] = {R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitation_list_detail);

        invitationsList = (ListView) findViewById(R.id.invitationsListView);
        InvitationsListView invitationsListView = new InvitationsListView(this, activityName, activityDateTime, userImage);
        invitationsList.setAdapter(invitationsListView);

        invitationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), IndividialInvitationUI.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(getApplicationContext(), IndividialInvitationUI.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), IndividialInvitationUI.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(getApplicationContext(), IndividialInvitationUI.class);
                    startActivity(intent);
                }
                if (position == 4) {
                    Intent intent = new Intent(getApplicationContext(), IndividialInvitationUI.class);
                    startActivity(intent);
                }
            }
        });
    }
}