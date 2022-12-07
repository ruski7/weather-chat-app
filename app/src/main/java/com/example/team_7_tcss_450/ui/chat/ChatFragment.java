package com.example.team_7_tcss_450.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentChatBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.chat.model.ChatSendViewModel;
import com.example.team_7_tcss_450.ui.chat.model.ChatViewModel;

import java.util.Objects;

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

        // Add "add new chat" icon to top menu bar
        MenuHost menuHost = requireActivity();
        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.chat_action_menu, menu);
            }
            // Handle the menu selection
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.chat_add_member: {
                        return true;
                    }
                    default:
                        return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

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
                    int viewPosition = rv.getAdapter().getItemCount() - 1;
                    viewPosition = Math.max(viewPosition, 0);
                    try {
                        rv.getAdapter().notifyDataSetChanged();
                        Log.d("CHAT", "Chat Messages RecyclerView Position: " + viewPosition);
                        Log.d("CHAT", "Messages List Size = " + list.size());
                        try {
                            Log.d("CHAT", "List end message = " + list.get(list.size() - 1).getMessage());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.w("CHAT", "List end out of bounds");
                        }

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
