package com.IrisBICS.lonelysg;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class InvitationsListView extends ArrayAdapter<String> {

    private String[] activityName;
    private String[] activityDateTime;
    private int userImage[];
    private Activity context;

        public InvitationsListView(Activity context, String activityName[], String activityDateTime[], int userImage[]) {
            super(context, R.layout.invitation_list_layout, activityName);
            this.context = context;
            this.activityName = activityName;
            this.activityDateTime = activityDateTime;
            this.userImage = userImage;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder = null;

            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(r==null) {
                r = layoutInflater.inflate(R.layout.invitation_list_layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }

            else
                r = layoutInflater.inflate(R.layout.invitation_list_layout,parent,false);
                viewHolder = (ViewHolder) r.getTag();
                viewHolder.activityName.setText(activityName[position]);
                viewHolder.activityDateTime.setText(activityDateTime[position]);
                viewHolder.userImage.setImageResource(userImage[position]);

            return r;
        }

        class ViewHolder {
            TextView activityName;
            TextView activityDateTime;
            ImageView userImage;

            ViewHolder(View v) {
                activityName = (TextView) v.findViewById(R.id.activityName);
                activityDateTime = (TextView) v.findViewById(R.id.activityDateTime);
                userImage = (ImageView) v.findViewById(R.id.userImage);
            }
        }
    }


