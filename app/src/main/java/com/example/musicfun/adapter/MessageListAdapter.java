package com.example.musicfun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Message;

import org.w3c.dom.Text;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;
    private String chatPartnerName;
    //private String actualDate;
    //private int[] positionChecked;//wurde diese position bereits Überpüft?0 = Nein, 1 = Ja
    //TODO: Muss auf Serverseite mit datum geprüft werden weil hier RecyclerView ist

    public MessageListAdapter(Context context, List<Message> messageList, String chatPartnerName){
        mContext = context;
        mMessageList = messageList;
        this.chatPartnerName = chatPartnerName;
        //actualDate = "01.01.2000";
        //positionChecked = new int[messageList.size()];
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        if (message.getSender().equals(chatPartnerName)) {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_view_my_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_view_other_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message, position);
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText, dateText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_other);
        }

        void bind(Message message, int pos) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            //TODO: iwie checken ob der aktuelle Tag ist

            // if(message.getDate().equals(actualDate) && positionChecked[pos] == 0){
            //     dateText.setVisibility(View.GONE);
            //     positionChecked[pos] = 1;
            // }
            dateText.setText(message.getDate());
            //actualDate = message.getDate();
            timeText.setText(message.getTime());
            nameText.setText(message.getSender());

        }
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, dateText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_me);
        }

        void bind(Message message, int pos) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());

           // if(message.getDate().equals(actualDate) && positionChecked[pos] == 0){
           //     dateText.setVisibility(View.GONE);
           //     positionChecked[pos] = 1;
           // }
            //Log.d("DateStyle", positionChecked[pos] + "");
            dateText.setText(message.getDate());
            //actualDate = message.getDate();
        }
    }

}


