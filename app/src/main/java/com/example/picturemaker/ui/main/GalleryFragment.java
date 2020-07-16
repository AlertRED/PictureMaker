package com.example.picturemaker.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.picturemaker.FirebaseDB;
import com.example.picturemaker.adapters.AdapterGalleryRV;
import com.example.picturemaker.FilterGalleryActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.support.Item;

import java.util.List;

public class GalleryFragment extends Fragment {

    RecyclerView rvMain;
    private AdapterGalleryRV rvMain_adapter;

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

    private void RefreshAdapter(List<Item> items) {
        rvMain_adapter = new AdapterGalleryRV(this.getContext() ,R.layout.item_pictute_gallery, items,30,30, false);
        rvMain.setAdapter(rvMain_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        TextView filter_view = (TextView) this.getActivity().findViewById(R.id.filter_view);

        filter_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterGalleryActivity.class);
                startActivity(intent);
            }
        });

        Spinner category = (Spinner) this.getActivity().findViewById(R.id.spinner);
        String[] items = new String[]{"Сначало популярное","Оценки по убыванию", "Оценки по возрастанию", "Сложность по убыванию", "Сложность по возрастанию"};
        ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        category.setAdapter(category_adapter);

        rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rv_gallery);
        rvMain.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvMain_adapter = new AdapterGalleryRV();
        FirebaseDB.loadItem(this::RefreshAdapter);
    }

}