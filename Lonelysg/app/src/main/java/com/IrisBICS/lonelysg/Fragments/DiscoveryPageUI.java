package com.IrisBICS.lonelysg.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.IrisBICS.lonelysg.Activities.InvitationsUI;
import com.IrisBICS.lonelysg.R;

public class DiscoveryPageUI extends Fragment implements View.OnClickListener {
    private CardView foodIcon, movieIcon, othersIcon, sportsIcon, studyIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discovery, container, false);

        // Defining cards
        foodIcon = v.findViewById(R.id.foodIcon);
        movieIcon = v.findViewById(R.id.movieIcon);
        othersIcon = v.findViewById(R.id.othersIcon);
        sportsIcon = v.findViewById(R.id.sportsIcon);
        studyIcon = v.findViewById(R.id.studyIcon);

        // Add Click listener to the cards
        foodIcon.setOnClickListener(this);
        movieIcon.setOnClickListener(this);
        othersIcon.setOnClickListener(this);
        sportsIcon.setOnClickListener(this);
        studyIcon.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.foodIcon :
                intent = new Intent(this.getActivity(), InvitationsUI.class);
                intent.putExtra("category", "Food and Drinks");
                startActivity(intent);
                break;

            case R.id.movieIcon :
                intent = new Intent(this.getActivity(), InvitationsUI.class);
                intent.putExtra("category", "Movies");
                startActivity(intent);
                break;

            case R.id.othersIcon :
                intent = new Intent(this.getActivity(), InvitationsUI.class);
                intent.putExtra("category", "Others");
                startActivity(intent);
                break;

            case R.id.sportsIcon :
                intent = new Intent(this.getActivity(), InvitationsUI.class);
                intent.putExtra("category", "Sports");
                startActivity(intent);
                break;

            case R.id.studyIcon :
                intent = new Intent(this.getActivity(), InvitationsUI.class);
                intent.putExtra("category", "Study");
                startActivity(intent);
                break;

            default :
                break;
        }
    }
}