package com.example.team_7_tcss_450.ui.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentChatRoomCardBinding;
import com.example.team_7_tcss_450.databinding.FragmentChatRoomsListBinding;
import com.example.team_7_tcss_450.ui.chat.model.ChatPreview;

import java.util.List;
import java.util.Map;

public class ChatPreviewRecyclerViewAdapter extends RecyclerView.Adapter<ChatPreviewRecyclerViewAdapter.ChatPreviewViewHolder> {

    private final List<ChatPreview> chatPreviewList;
    private OnChatPreviewListener mOnChatPreviewListener;

    public ChatPreviewRecyclerViewAdapter(final List<ChatPreview> chatPreviews, OnChatPreviewListener onChatPreviewListener) {
        this.chatPreviewList = chatPreviews;
        this.mOnChatPreviewListener = onChatPreviewListener;
    }

    @NonNull
    @Override
    public ChatPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_room_card, parent, false);
        return new ChatPreviewViewHolder(view, mOnChatPreviewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatPreviewViewHolder holder, int position) {
        FragmentChatRoomCardBinding binding = holder.binding;
        ChatPreview chatPreview = chatPreviewList.get(position);
        binding.chatName.setText(chatPreview.getChatName());
        binding.latestSenderName.setText(chatPreview.getLatestSender());
        binding.latestMessage.setText(chatPreview.getLatestMessage());
        binding.latestMessageDate.setText(chatPreview.getLatestSendDate());

        Log.d("CHAT", "RecyclerView onBindViewHolder() called");
    }

    @Override
    public int getItemCount() {
        return chatPreviewList.size();
    }

    public class ChatPreviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mView;
        public FragmentChatRoomCardBinding binding;
        OnChatPreviewListener onChatPreviewListener;
        public ChatPreviewViewHolder(View view, OnChatPreviewListener onChatPreviewListener) {
            super(view);
            mView = view;
            binding = FragmentChatRoomCardBinding.bind(view);
            this.onChatPreviewListener = onChatPreviewListener;
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onChatPreviewListener.onPreviewClick(getBindingAdapterPosition());
            Log.d("CHAT RECYCLER", "On Click called at item position: " + getBindingAdapterPosition());
        }

    }

    public interface OnChatPreviewListener {
        void onPreviewClick(int position);
    }

}
