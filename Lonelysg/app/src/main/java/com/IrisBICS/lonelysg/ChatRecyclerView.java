package com.IrisBICS.lonelysg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatRecyclerView {

    public static final int SENDER_MESSAGE = 1;
    public static final int RECEIVER_MESSAGE = 0;

    String currentUser;

    private Context context;
    private MessageAdapter messageAdapter;

    public void setConfig(RecyclerView recyclerView, IndividualChatUI c, List<Message> messages, List<String> keys){
        context = c;
        messageAdapter = new MessageAdapter(messages,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(messageAdapter);
    }

    class MessageView extends RecyclerView.ViewHolder{

        private TextView user;
        private TextView message;

        private String key;


        public MessageView(View view) {
            super(view);
            user = (TextView) itemView.findViewById(R.id.user);
            message = (TextView) itemView.findViewById(R.id.message);

        }

        public void bind(Message m, String key){
            message.setText(m.getMessage());
            user.setText(m.getSender());
        }

    }

    class MessageAdapter extends RecyclerView.Adapter<MessageView>{

        private List<Message> messageList;
        private List<String> keys;

        public MessageAdapter(List<Message> messageList, List<String> keys) {
            this.messageList = messageList;
            this.keys = keys;
        }

        @Override
        public MessageView onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == SENDER_MESSAGE){
                View view = LayoutInflater.from(context).inflate(R.layout.chat_view_sender, parent, false);
                return new MessageView(view);
            }
            else {
                View view = LayoutInflater.from(context).inflate(R.layout.chat_view_receiver, parent, false);
                return new MessageView(view);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull MessageView holder, int position) {
            holder.bind(messageList.get(position), keys.get(position));
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        public int getItemViewType(int position){
            currentUser = FirebaseDatabaseHelper.getCurrentUser();
            if (messageList.get(position).getSender().equals(currentUser)){
                return SENDER_MESSAGE;
            }
            else return RECEIVER_MESSAGE;
        }
    }



}
