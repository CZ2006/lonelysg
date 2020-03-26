package com.IrisBICS.lonelysg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.Activities.NavigationBarUI;

public class IndividualInvitationUI extends AppCompatActivity {

    Button acceptInvitation, rejectInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_invitation_ui);

        acceptInvitation = (Button)findViewById(R.id.acceptInvitation);
        rejectInvitation = (Button)findViewById(R.id.rejectInvitation);

        // Pressing request button
        acceptInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IndividualInvitationUI.this, "Request Sent", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (IndividualInvitationUI.this, NavigationBarUI.class);
                startActivity(i);
            }
        });

        // Pressing cancel button
        rejectInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IndividualInvitationUI.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (IndividualInvitationUI.this, NavigationBarUI.class);
                startActivity(i);
            }
        });
    }
}
