package com.example.team_7_tcss_450.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentChatRoomsListBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.chat.model.ChatPreview;
import com.example.team_7_tcss_450.ui.chat.model.ChatViewModel;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChatPreviewsFragment extends Fragment implements ChatPreviewsRecyclerViewAdapter.OnChatPreviewListener {

    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;

    public ChatPreviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mChatModel = provider.get(ChatViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
        Log.d("CHAT", "Chat Room List onCreate() called");
        if (mChatModel.getChatPreviewsList().isEmpty()) {
            mChatModel.connectGetChatPreviews(mUserModel.getEmail(), mUserModel.getJWT());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_rooms_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentChatRoomsListBinding binding = FragmentChatRoomsListBinding.bind(requireView());

        final RecyclerView rv = binding.recyclerChatPreviews;
        rv.setAdapter(new ChatPreviewsRecyclerViewAdapter(mChatModel.getChatPreviewsList(), this));

        mChatModel.addChatPreviewsObserver(getViewLifecycleOwner(), chatPreviews -> {
            rv.setAdapter(new ChatPreviewsRecyclerViewAdapter(chatPreviews, this));
        });

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
                    case R.id.add_new_chat: {
                        // TODO: Implement add new chat XML sheet and navigation to new chat
                        return true;
                    }
                    default:
                        return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onPreviewClick(int position) {
        ChatPreview chatPreview = mChatModel.getChatPreviewsList().get(position);
        int chatId = chatPreview.getChatId();
        ChatPreviewsFragmentDirections.ActionNavigationMessageToChatFragment actionNavigateToChat =
                ChatPreviewsFragmentDirections.actionNavigationMessageToChatFragment(chatId);
        Navigation.findNavController(requireView()).navigate(actionNavigateToChat);
        Log.d("CHAT", "Latest message: " + chatPreview.getLatestMessage());
    }
}