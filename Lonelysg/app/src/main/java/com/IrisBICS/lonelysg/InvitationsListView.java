package com.IrisBICS.lonelysg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class InvitationsListView extends ArrayAdapter<String> {

    private String[] activityName;
    private String[] activityDateTime;
    private Activity context;

        public InvitationsListView(Activity context, String[] activityName, String[] activityDateTime) {
            super(context, R.layout.invitation_list_layout,activityName);
            this.context = context;
            this.activityName = activityName;
            this.activityDateTime = activityDateTime;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder = null;
            if(r==null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.invitation_list_layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else
                viewHolder = (ViewHolder) r.getTag();
            viewHolder.activityName.setText(activityName[position]);
            viewHolder.activityDateTime.setText(activityDateTime[position]);

            return r;
        }

        class ViewHolder {
            TextView activityName;
            TextView activityDateTime;
            ViewHolder(View v) {
                activityName = (TextView) v.findViewById(R.id.activityName);
                activityDateTime = (TextView) v.findViewById(R.id.activityDateTime);
            }
        }

    }


