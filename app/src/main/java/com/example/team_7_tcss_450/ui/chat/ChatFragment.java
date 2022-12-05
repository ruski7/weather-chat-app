package com.example.team_7_tcss_450.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentChatBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.chat.model.ChatSendViewModel;
import com.example.team_7_tcss_450.ui.chat.model.ChatViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    //The chat ID for "global" chat
    private static int mChatId;

    private ChatSendViewModel mSendModel;
    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatId = ChatFragmentArgs.fromBundle(getArguments()).getChatId();
        Log.d("CHAT", "ChatFragment OnCreate() called");
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        if (mChatModel.getMessageListByChatId(mChatId).isEmpty()) {
            mChatModel.getFirstMessages(mChatId, mUserModel.getJWT());
        }
        mSendModel = provider.get(ChatSendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatBinding binding = FragmentChatBinding.bind(requireView());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        Log.d("CHAT", "onViewCreated got called. ChatRecyclerView is being instantiated");
        rv.setAdapter(new ChatRecyclerViewAdapter(
                        mChatModel.getMessageListByChatId(mChatId),
                        mUserModel.getEmail()));


        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(mChatId, mUserModel.getJWT());
        });

        mChatModel.addMessageObserver(mChatId, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    //rv.getAdapter().notifyDataSetChanged(); The try catch statement below is a more time-efficient version of this line
                    int viewPosition = mChatModel.getMessageListByChatId(mChatId).size() - 1;
                    try {
                        rv.getAdapter().notifyItemInserted(viewPosition);
                        Log.d("CHAT", "Position: " + (rv.getAdapter().getItemCount() - 1));
                    } catch (NullPointerException e) {
                        Log.w("CHAT", "notifyItemRangeInserted() returned null pointer exception." +
                                "This is likely because the data list connected to our recycler view adapter is empty.");
                    }

                    rv.scrollToPosition(viewPosition);
                    binding.swipeContainer.setRefreshing(false);
                });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            mSendModel.sendMessage(mChatId,
                    mUserModel.getJWT(),
                    binding.editMessage.getText().toString());
        });
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response -> {

            binding.editMessage.setText("");
        });
    }
}
