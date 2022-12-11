package com.example.team_7_tcss_450.ui.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.MainActivity;
import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentForecastListBinding;
import com.example.team_7_tcss_450.databinding.FragmentHomeBinding;
import com.example.team_7_tcss_450.databinding.FragmentSignInBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.weather.model.WeeklyForecastViewModel;
import com.example.team_7_tcss_450.model.LocationViewModel;

/**
 * A fragment representing a list of Items.
 */
public class WeeklyForecastFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private WeeklyForecastViewModel mForecastListModel;
    private UserInfoViewModel mUserModel;
    private LocationViewModel mLocation;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeeklyForecastFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("fragment_made", "OnCreate Called in ForecastListFragment");
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mUserModel = provider.get(UserInfoViewModel.class);

        mForecastListModel = new ViewModelProvider(requireActivity()).get(WeeklyForecastViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentForecastListBinding binding = FragmentForecastListBinding.bind(requireView());

        mLocation = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            binding.list.setLayoutManager(new LinearLayoutManager(context));
        } else {
            binding.list.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        final RecyclerView rv = binding.list;
        mForecastListModel.addDailyForecastListObserver(getViewLifecycleOwner(), (forecastList) -> {
            rv.setAdapter(new WeeklyForecastRecyclerViewAdapter(forecastList));
            if (forecastList.isEmpty())

                mForecastListModel.connectGetWeeklyForecast(mUserModel.getJWT(), mLocation.getCurrentLocation());
        });

    }

}