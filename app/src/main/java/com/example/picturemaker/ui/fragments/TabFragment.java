package com.example.picturemaker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.picturemaker.R;
import com.example.picturemaker.adapters.AdapterCollectionRV;
import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.support.PictureDiffUtilCallback;

import java.util.List;
import java.util.Objects;

public class TabFragment extends Fragment {

    String collectionType;
    private AdapterCollectionRV rvMainAdapter;
    private Storage storage;

    public TabFragment(String collectionType) {
        this.collectionType = collectionType;
        this.storage = Storage.getInstance(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection_tab, container, false);
    }

    private void RefreshAdapter(List<Picture> pictures) {
        PictureDiffUtilCallback pictureDiffUtilCallback = new PictureDiffUtilCallback(rvMainAdapter.getData(), pictures);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(pictureDiffUtilCallback);
        rvMainAdapter.setData(pictures);
        productDiffResult.dispatchUpdatesTo(rvMainAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView rvMain = (RecyclerView) Objects.requireNonNull(getView()).findViewById(R.id.rv_collection);

        ((SimpleItemAnimator) Objects.requireNonNull(rvMain.getItemAnimator())).setSupportsChangeAnimations(false);
        rvMainAdapter = new AdapterCollectionRV(this.getContext(), R.layout.item_pictute_gallery, 30, 30, false);
        rvMain.setAdapter(rvMainAdapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvMain.setLayoutManager(layoutManager);

        LiveData<List<Picture>> liveData = this.storage.GetLiveDataFromView(collectionType);
        liveData.observe(getViewLifecycleOwner(), this::RefreshAdapter);
        this.storage.LoadPicturesByCollection(collectionType);
    }
}