package com.example.picturemaker.ui.main;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.FilterGalleryActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.adapters.AdapterGalleryRV;
import com.example.picturemaker.storage.Storage;

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
    public void onResume() {
        super.onResume();
        rvMain_adapter.notifyDataSetChanged();
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
        this.storage.GetPicturesIds(this::RefreshAdapter, parameters);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            rvMain_adapter.notifyDataSetChanged();
    }

    private void RefreshAdapter(List<String> picturesIds) {
        rvMain_adapter = new AdapterGalleryRV(this.getContext(), R.layout.item_pictute_gallery, picturesIds, 30, 30, false);
        rvMain.setAdapter(rvMain_adapter);
        rvMain_adapter.notifyDataSetChanged();
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
            intent.putExtra("act", this.author);
            intent.putExtra("level", this.level);
            intent.putExtra("genre", this.genre);
            startActivityForResult(intent, 0);
        });

        Spinner category = (Spinner) this.getActivity().findViewById(R.id.spinner);
        String[] items = new String[]{"Сначало популярное", "Оценки по убыванию", "Оценки по возрастанию", "Сложность по убыванию", "Сложность по возрастанию"};
        ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        category.setAdapter(category_adapter);

        rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rv_gallery);
        rvMain.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvMain_adapter = new AdapterGalleryRV();
        this.storage.GetPicturesIds(this::RefreshAdapter, new Hashtable<>());
    }

}