package com.example.picturemaker.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.picturemaker.AdapterGalleryRV;
import com.example.picturemaker.R;
import com.example.picturemaker.TestData;

public class RecentlyFragment extends Fragment {

//    private MainViewModel mViewModel;

    public static RecentlyFragment newInstance() {
        return new RecentlyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recently_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        int[] images = {
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4,
                R.drawable.image5,
                R.drawable.image6,
                R.drawable.image7,
                R.drawable.image8,
                R.drawable.image9,
                R.drawable.image10};

        String[] values = {"Картина№1", "Картина№2", "Картина№3",
                "Картина№4","Картина№5","Картина№6",
                "Картина№7","Картина№8","Картина№9","Картина№10"};

        RecyclerView rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rv_recently);
        AdapterGalleryRV rvMain_adapter = new AdapterGalleryRV(R.layout.pictute_item_gallery, 30,30, false);
        rvMain.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvMain.setAdapter(rvMain_adapter);
    }

}