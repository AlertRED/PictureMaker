package com.example.picturemaker.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.adapters.AdapterCollectionRV;
import com.example.picturemaker.R;

import java.util.List;

public class RecentlyFragment extends Fragment {
    private AdapterCollectionRV rvMain_adapter;
    private RecyclerView rvMain;
    private Storage storage;

//    private MainViewModel mViewModel;

    public static RecentlyFragment newInstance() {
        return new RecentlyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recently, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        rvMain_adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            rvMain_adapter.notifyDataSetChanged();
    }

    private void RefreshAdapter(List<Picture> pictures) {
        rvMain_adapter = new AdapterCollectionRV(this.getContext() ,R.layout.item_pictute_gallery, pictures,30,30, false);
        rvMain.setAdapter(rvMain_adapter);
        rvMain_adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        this.storage = Storage.getInstance(this.getContext());

//        LiveData<String> liveData = DataController
//
//        liveData.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String value) {
//                textView.setText(value)
//            }
//        });

        rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rv_recently);
        rvMain.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvMain_adapter = new AdapterCollectionRV();
        LiveData<List<Picture>> liveData = this.storage.GetPicturesLiveData();
        liveData.observe(getViewLifecycleOwner(), pictures -> RefreshAdapter(pictures));
    }

}