package com.example.picturemaker.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.picturemaker.FilterGalleryActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.adapters.AdapterGalleryRV;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.support.PictureDiffUtilCallback;
import com.example.picturemaker.support.SpacesItemDecoration;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment {

    RecyclerView recyclerView;
    private AdapterGalleryRV adapterGalleryRV;
    private Storage storage;
    private String author;
    private String genre;
    private String level;
    private Map<String, Object> parametersFilter = new Hashtable<>();
    private Map<String, Object> parametersSort = new Hashtable<>();
    private Spinner category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            this.genre = data.getStringExtra("genre");
            this.level = data.getStringExtra("level");
            this.author = data.getStringExtra("author");

            Map<String, Object> parameters = new Hashtable<>();

            if (!this.level.equals(this.getString(R.string.any)))
                parameters.put("level", this.level);
            if (!this.genre.equals(this.getString(R.string.any)))
                parameters.put("genre", this.genre);
            if (!this.author.equals(this.getString(R.string.any)))
                parameters.put("author", this.author);

            this.parametersFilter = parameters;
            this.refreshData();
        }
    }

    private void refreshData() {
        this.storage.LoadPicturesByGallery(this.parametersFilter, this.parametersSort);
    }

    private void RefreshAdapter(List<Picture> pictures) {
        PictureDiffUtilCallback pictureDiffUtilCallback = new PictureDiffUtilCallback(adapterGalleryRV.getData(), pictures);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(pictureDiffUtilCallback);
        adapterGalleryRV.setData(pictures);
        productDiffResult.dispatchUpdatesTo(adapterGalleryRV);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.storage = Storage.getInstance(this.getContext());
        TextView filter_view = (TextView) Objects.requireNonNull(this.getActivity()).findViewById(R.id.filter_view);

        filter_view.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FilterGalleryActivity.class);
            intent.putExtra("author", this.author);
            intent.putExtra("level", this.level);
            intent.putExtra("genre", this.genre);
            startActivityForResult(intent, 0);
        });

        recyclerView = (RecyclerView) Objects.requireNonNull(this.getActivity()).findViewById(R.id.rv_gallery);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        adapterGalleryRV = new AdapterGalleryRV(this.getContext(), R.layout.item_pictute_gallery, 30, 30, false);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.addItemDecoration(new SpacesItemDecoration(16));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterGalleryRV);

        LiveData<List<Picture>> liveData = this.storage.GetLiveDataFromView("Gallery");
        liveData.observe(getViewLifecycleOwner(), this::RefreshAdapter);


        this.category = (Spinner) Objects.requireNonNull(this.getActivity()).findViewById(R.id.spinner);
        String[] items = new String[]{this.getString(R.string.sort_descending_ratings),
                this.getString(R.string.sort_ascending_ratings),
                this.getString(R.string.sort_descending_difficulty),
                this.getString(R.string.sort_ascending_difficulty)};

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()), android.R.layout.simple_spinner_dropdown_item, items);
        this.category.setAdapter(category_adapter);
        this.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, Object> parameters = new HashMap<>();
                switch (i) {
                    case 0:
                        parameters.put("order_field", "total_score");
                        parameters.put("is_asc", false);
                        break;
                    case 1:
                        parameters.put("order_field", "total_score");
                        parameters.put("is_asc", true);
                        break;
                    case 2:
                        parameters.put("order_field", "level_id");
                        parameters.put("is_asc", false);
                        break;
                    case 3:
                        parameters.put("order_field", "level_id");
                        parameters.put("is_asc", true);
                        break;
                }
                parametersSort = parameters;
                refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}