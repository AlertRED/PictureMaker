package com.example.picturemaker.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.picturemaker.support.SpacesItemDecoration;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    RecyclerView rvMain;
    private AdapterGalleryRV rvMain_adapter;
    private Storage storage;
    private String author;
    private String genre;
    private String level;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    private void RefreshAdapter(List<Picture> pictures) {
        PictureDiffUtilCallback pictureDiffUtilCallback = new PictureDiffUtilCallback(rvMain_adapter.getData(), pictures);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(pictureDiffUtilCallback);
        rvMain_adapter.setData(pictures);
        productDiffResult.dispatchUpdatesTo(rvMain_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        this.storage = Storage.getInstance(this.getContext());
        TextView filter_view = (TextView) this.getActivity().findViewById(R.id.filter_view);

        filter_view.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FilterGalleryActivity.class);
            intent.putExtra("author", this.author);
            intent.putExtra("level", this.level);
            intent.putExtra("genre", this.genre);
            startActivityForResult(intent, 0);
        });

        Spinner category = (Spinner) this.getActivity().findViewById(R.id.spinner);
        String[] items = new String[]{"Сначало популярное", "Оценки по убыванию", "Оценки по возрастанию", "Сложность по убыванию", "Сложность по возрастанию"};
        ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        category.setAdapter(category_adapter);

        rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rv_gallery);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.default_gaps);
//        rvMain.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        ((SimpleItemAnimator) rvMain.getItemAnimator()).setSupportsChangeAnimations(false);
        rvMain_adapter = new AdapterGalleryRV(this.getContext(), getResources(),R.layout.item_pictute_gallery, 30, 30, false);
        rvMain.setAdapter(rvMain_adapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvMain.setLayoutManager(layoutManager);

        LiveData<List<Picture>> liveData = this.storage.GetLiveDataFromView("Gallery");
        liveData.observe(getViewLifecycleOwner(), this::RefreshAdapter);
        this.storage.LoadPicturesByGallery(new Hashtable<>());
    }
}