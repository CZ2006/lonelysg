package com.IrisBICS.lonelysg;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ChatListView extends ArrayAdapter<String> {

    private List<String> chatUsers;
    private Activity context;

    public ChatListView(Activity context, List<String> users) {
        super(context, R.layout.chat_list_layout, users);
        this.context = context;
        this.chatUsers = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
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
