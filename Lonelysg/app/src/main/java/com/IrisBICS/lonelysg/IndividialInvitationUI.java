package com.IrisBICS.lonelysg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class IndividialInvitationUI extends AppCompatActivity {

    Button acceptInvitation, rejectInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individial_invitation_u_i);

        acceptInvitation = (Button)findViewById(R.id.acceptInvitation);
        rejectInvitation = (Button)findViewById(R.id.rejectInvitation);

        // Pressing request button
        acceptInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IndividialInvitationUI.this, "Request Sent", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (IndividialInvitationUI.this, NavigationBarUI.class);
                startActivity(i);
            }
        });

        // Pressing cancel button
        rejectInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IndividialInvitationUI.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (IndividialInvitationUI.this, NavigationBarUI.class);
                startActivity(i);
            }
        });
    }
}
