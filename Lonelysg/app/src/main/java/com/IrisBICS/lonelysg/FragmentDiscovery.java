package com.IrisBICS.lonelysg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class FragmentDiscovery extends Fragment implements View.OnClickListener {
    private CardView foodIcon, movieIcon, othersIcon, sportsIcon, studyIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discovery, container, false);

        // Defining cards
        foodIcon = (CardView) v.findViewById(R.id.foodIcon);
        movieIcon = (CardView) v.findViewById(R.id.movieIcon);
        othersIcon = (CardView) v.findViewById(R.id.othersIcon);
        sportsIcon = (CardView) v.findViewById(R.id.sportsIcon);
        studyIcon = (CardView) v.findViewById(R.id.studyIcon);

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
        Intent i;

        switch (v.getId()) {
            case R.id.foodIcon :
                i = new Intent(this.getActivity(), InvitationsUI.class);
                startActivity(i);
                break;

            case R.id.movieIcon :
                i = new Intent(this.getActivity(), InvitationsUI.class);
                startActivity(i);
                break;

            case R.id.othersIcon :
                i = new Intent(this.getActivity(), InvitationsUI.class);
                startActivity(i);
                break;

            case R.id.sportsIcon :
                i = new Intent(this.getActivity(), InvitationsUI.class);
                startActivity(i);
                break;

            case R.id.studyIcon :
                i = new Intent(this.getActivity(), InvitationsUI.class);
                startActivity(i);
                break;

            default :
                break;
        }
    }
}
