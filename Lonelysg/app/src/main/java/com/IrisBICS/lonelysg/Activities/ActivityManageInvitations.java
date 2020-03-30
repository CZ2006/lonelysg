package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.IrisBICS.lonelysg.R;

public class ActivityManageInvitations extends AppCompatActivity {

    private CardView createInvitation, viewInvitations;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invitations_ui);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createInvitation = findViewById(R.id.createInvitation);
        createInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ActivityManageInvitations.this, ActivityCreateInvitation.class);
                startActivity(intent);
            }
        });

        viewInvitations = findViewById(R.id.viewInvitations);
        viewInvitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ActivityManageInvitations.this, ActivityUserInvitations.class);
                startActivity(intent);
            }
        });

    }
}
