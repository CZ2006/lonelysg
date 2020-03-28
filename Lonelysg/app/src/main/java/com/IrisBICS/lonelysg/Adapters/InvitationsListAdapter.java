package com.IrisBICS.lonelysg.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.IrisBICS.lonelysg.Models.Invitation;
import com.IrisBICS.lonelysg.R;

import java.util.ArrayList;


public class InvitationsListAdapter extends ArrayAdapter<Invitation> {

        private ArrayList<Invitation> invitationsList;
//        private int userImage[];
        private Activity context;

        public InvitationsListAdapter(Activity context, ArrayList<Invitation> invitations) {
            super(context, R.layout.invitation_list_layout,invitations);
            this.context = context;
            this.invitationsList = invitations;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder;
            if(r==null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.invitation_list_layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else viewHolder = (ViewHolder) r.getTag();
            viewHolder.invitationTitle.setText(invitationsList.get(position).getTitle());
            viewHolder.invitationDateTime.setText(invitationsList.get(position).getDate()+" " + invitationsList.get(position).getStartTime()+" - " + invitationsList.get(position).getEndTime());
//            viewHolder.userImage.setImageResource(userImage[position]);
            return r;
        }

        class ViewHolder {
            TextView invitationTitle;
            TextView invitationDateTime;
//            ImageView userImage;

            ViewHolder(View v) {
                invitationTitle = v.findViewById(R.id.invitationTitle);
                invitationDateTime = v.findViewById(R.id.invitationDateTime);
//                userImage = v.findViewById(R.id.userImage);
            }
        }
    }


