package com.example.picturemaker.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.picturemaker.Adaptery;
import com.example.picturemaker.R;

public class HomeFragment extends Fragment {

    private MainViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    int[] images = {R.drawable.ic_baseline_gallery_24,
            R.drawable.ic_baseline_favorite_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_profile_24,
            R.drawable.ic_baseline_recently_24};

    String[] values = {"слово", "слово", "слово", "слово", "слово"};

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
        GridView gridView = this.getView().findViewById(R.id.grid_gallery);
        gridView.setAdapter(new Adaptery(this.getContext(), values, images));
        gridView.setNumColumns(2);
    }

}