package com.IrisBICS.lonelysg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.IrisBICS.lonelysg.R;

public class InvitationsUI extends AppCompatActivity {

    ListView invitationsList;
    String[] activityName = {"Example 1", "Example 2", "Example 3", "Example 4","Example 5"};
    String[] activityDateTime = {"12pm - 1pm, 1 April 2020", "2pm - 4pm, 5 April 2020", "11am - 1pm, 11 April 2020", "10pm - 10.30pm, 15 April 2020", "10am - 11am, 19 April 2020"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitation_list_detail);

        invitationsList = (ListView) findViewById(R.id.invitationsListView);
        InvitationsListView invitationsListView = new InvitationsListView(this,activityName,activityDateTime);
        invitationsList.setAdapter(invitationsListView);
    }
}