package com.example.team_7_tcss_450.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.ui.chat.ChatFragment;
import com.example.team_7_tcss_450.ui.weather.WeeklyForecastFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatHomeFragment extends Fragment {

    final static int NUMBER_OF_TABS = 2;

    // When requested, this adapter returns a chat fragment, be it a chat room list or a specific chat room
    ChatHomeStateAdapter mChatHomeStateAdapter;
    ViewPager2 mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChatHomeStateAdapter = new ChatHomeStateAdapter(this);
        mViewPager = view.findViewById(R.id.chat_tabs_pager);
        mViewPager.setAdapter(mChatHomeStateAdapter);
        TabLayout tabLayout = view.findViewById(R.id.chat_home_tabs);
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> {
            if (position == 0)
                tab.setText(R.string.label_chat_rooms);
            else
                tab.setText(R.string.label_chat);
        }).attach();
    }
}

class ChatHomeStateAdapter extends FragmentStateAdapter {

    public ChatHomeStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("CHAT TABS", "tab position: " + position);
        final Fragment fragmentToShow;
        if (position == 0) {
            Log.d("CHAT TABS", "Tab position 0 selected");
            return new WeeklyForecastFragment();
        } else {
            Log.d("CHAT TABS", "Tab position 1 selected");
            return new ChatFragment();
        }
    }

    @Override
    public int getItemCount() {
        return ChatHomeFragment.NUMBER_OF_TABS;
    }

}