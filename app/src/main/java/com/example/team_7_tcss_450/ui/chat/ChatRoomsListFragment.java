package com.example.team_7_tcss_450.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentChatRoomsListBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.chat.ChatPreviewRecyclerViewAdapter;
import com.example.team_7_tcss_450.ui.chat.model.ChatPreview;
import com.example.team_7_tcss_450.ui.chat.model.ChatViewModel;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChatRoomsListFragment extends Fragment implements ChatPreviewRecyclerViewAdapter.OnChatPreviewListener {

    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;

    public ChatRoomsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mChatModel = provider.get(ChatViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
        Log.d("CHAT", "Chat Room List onCreate() called");
        if (mChatModel.mChatPreviewsList.getValue().isEmpty()) {
            mChatModel.getChatPreviews(mUserModel.getEmail(), mUserModel.getJWT());
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
        rv.setAdapter(new ChatPreviewRecyclerViewAdapter(mChatModel.mChatPreviewsList.getValue(), this));

        mChatModel.addChatPreviewsObserver(getViewLifecycleOwner(), chatPreviews -> {
            rv.setAdapter(new ChatPreviewRecyclerViewAdapter(chatPreviews, this));
        });

    }

    @Override
    public void onPreviewClick(int position) {
        ChatPreview chatPreview = mChatModel.mChatPreviewsList.getValue().get(position);
        int chatId = chatPreview.getChatId();
        ChatRoomsListFragmentDirections.ActionNavigationMessageToChatFragment actionNavigateToChat =
                ChatRoomsListFragmentDirections.actionNavigationMessageToChatFragment(chatId);
        Navigation.findNavController(requireView()).navigate(actionNavigateToChat);
        Log.d("CHAT", "Latest message: " + chatPreview.getLatestMessage());
    }
}