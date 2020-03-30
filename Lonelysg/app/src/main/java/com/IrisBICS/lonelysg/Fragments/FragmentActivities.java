package com.IrisBICS.lonelysg.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.IrisBICS.lonelysg.Activities.ActivityManageInvitations;
import com.IrisBICS.lonelysg.Activities.ActivityManageRequests;
import com.IrisBICS.lonelysg.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class FragmentActivities extends Fragment {

    private CardView invitationsIcon, requestsIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_activities, container, false);

        // Defining cards
        invitationsIcon = v.findViewById(R.id.invitationsIcon);
        requestsIcon = v.findViewById(R.id.requestsIcon);

        // Add Click listener to the cards
        invitationsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(FragmentActivities.this.getActivity(), ActivityManageInvitations.class);
                startActivity(intent);
            }
        });
        requestsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(FragmentActivities.this.getActivity(), ActivityManageRequests.class);
                startActivity(intent);
            }
        });

        return v;
    }

}
