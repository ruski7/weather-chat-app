package com.example.team_7_tcss_450.ui.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentReceivedMessageBinding;
import com.example.team_7_tcss_450.databinding.FragmentSentMessageBinding;
import com.example.team_7_tcss_450.ui.chat.model.ChatMessage;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT_MSG = 0;
    private static final int VIEW_TYPE_RECEIVED_MSG = 1;

    private final List<ChatMessage> mMessages;
    private final String mUserEmail;
    public ChatRecyclerViewAdapter(List<ChatMessage> messages, String email) {
        this.mMessages = messages;
        mUserEmail = email;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT_MSG) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVED_MSG) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        } else {
            // If the view type fails to return (which shouldn't ever happen) just default to sent message view
            return new SentMessageViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.fragment_sent_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = mMessages.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SENT_MSG:
                ((SentMessageViewHolder) holder).setMessage(msg);
                break;
            case VIEW_TYPE_RECEIVED_MSG:
                ((ReceivedMessageViewHolder) holder).setMessage(msg);
                break;
        }
    }

    @Override
    public int getItemCount() {
        try {
            return mMessages.size();
        } catch (Exception e) {
            Log.w("ChatRecycler", "Messages List empty for current chat room, returning 1");
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = mMessages.get(position);
        // If the email of the user who sent the message is equal to ourselves,
        // we return a view type corresponding to a user sent message,
        // otherwise we return a view type corresponding to a received message.
        return msg.getSender().equals(mUserEmail) ? VIEW_TYPE_SENT_MSG : VIEW_TYPE_RECEIVED_MSG;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final FragmentSentMessageBinding binding;

        public SentMessageViewHolder(@NonNull View view) {
            super(view);
            binding = FragmentSentMessageBinding.bind(view);
        }

        void setMessage(final ChatMessage message) {
            binding.textMessage.setText(message.getMessage());
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final FragmentReceivedMessageBinding binding;

        public ReceivedMessageViewHolder(@NonNull View view) {
            super(view);
            binding = FragmentReceivedMessageBinding.bind(view);
        }

        void setMessage(final ChatMessage msg) {
            binding.textMessage.setText(msg.getSender() +
                    ": " + msg.getMessage());
        }
    }

}
