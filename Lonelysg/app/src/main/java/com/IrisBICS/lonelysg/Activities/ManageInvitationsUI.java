package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.R;

public class ManageInvitationsUI extends AppCompatActivity {

    private Button createInvitation, viewInvitations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invitations_ui);

        createInvitation = findViewById(R.id.createInvitation);
        createInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ManageInvitationsUI.this, CreateInvitationUI.class);
                startActivity(intent);
            }
        });

        viewInvitations = findViewById(R.id.viewInvitations);
        viewInvitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ManageInvitationsUI.this, UserInvitationsUI.class);
                startActivity(intent);
            }
        });

    }
}
