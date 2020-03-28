package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.IrisBICS.lonelysg.R;

public class ManageRequestsUI extends AppCompatActivity {

    private CardView viewPending, viewReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests_ui);

        viewPending = findViewById(R.id.pendingRequests);
        viewPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ManageRequestsUI.this, PendingRequestsUI.class);
                startActivity(intent);
            }
        });

        viewReceived = findViewById(R.id.receivedRequestsTitle);
        viewReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ManageRequestsUI.this, ReceivedRequestsUI.class);
                startActivity(intent);
            }
        });

    }
}
