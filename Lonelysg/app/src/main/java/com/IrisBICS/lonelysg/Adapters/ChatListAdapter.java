package com.IrisBICS.lonelysg.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.IrisBICS.lonelysg.R;

import java.util.List;

public class ChatListAdapter extends ArrayAdapter<String> {

    private List<String> chatUsers;
    private Activity context;

    public ChatListAdapter(Activity context, List<String> users) {
        super(context, R.layout.chat_list_layout, users);
        this.context = context;
        this.chatUsers = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;
        if(r==null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.chat_list_layout,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) r.getTag();
        viewHolder.chatUser.setText(chatUsers.get(position));

        return r;
    }

    class ViewHolder {
        TextView chatUser;
        ViewHolder(View v) {
            chatUser = (TextView) v.findViewById(R.id.chatUser);
        }
    }

}
