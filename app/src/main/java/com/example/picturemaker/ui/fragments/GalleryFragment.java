package com.example.picturemaker.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.support.PictureDiffUtilCallback;

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
            System.err.println(requestCode);
            this.genre = data.getStringExtra("genre");
            this.level = data.getStringExtra("level");
            this.author = data.getStringExtra("author");

            Map<String, Object> parameters = new Hashtable<>();
            if (!this.level.equals("Любой")) {
                int num_level = 0;
                switch (this.level) {
                    case "Легкий":
                        num_level = 1;
                        break;
                    case "Средний":
                        num_level = 2;
                        break;
                    case "Сложный":
                        num_level = 3;
                        break;
                }
                parameters.put("level", num_level);
            }
            if (!this.genre.equals("Любой"))
                parameters.put("genre", this.genre);
            if (!this.author.equals("Любой")) {
                parameters.put("author", this.author);
            }
            this.storage.LoadPicturesByGallery(parameters);
        }
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

        Spinner category = (Spinner) Objects.requireNonNull(this.getActivity()).findViewById(R.id.spinner);
        String[] items = new String[]{"Сначало популярное", "Оценки по убыванию", "Оценки по возрастанию", "Сложность по убыванию", "Сложность по возрастанию"};
        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()), android.R.layout.simple_spinner_dropdown_item, items);
        category.setAdapter(category_adapter);

        recyclerView = (RecyclerView) this.getActivity().findViewById(R.id.rv_gallery);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        adapterGalleryRV = new AdapterGalleryRV(this.getContext(), R.layout.item_pictute_gallery, 30, 30, false);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterGalleryRV);

        LiveData<List<Picture>> liveData = this.storage.GetLiveDataFromView("Gallery");
        liveData.observe(getViewLifecycleOwner(), this::RefreshAdapter);
        this.storage.LoadPicturesByGallery(new Hashtable<>());
    }
}