package com.example.picturemaker.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.picturemaker.Adaptery;
import com.example.picturemaker.R;

public class GalleryFragment extends Fragment {

    RecyclerView rvMain;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gallery_fragment, container, false);
    }

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        Spinner category = (Spinner) this.getActivity().findViewById(R.id.spinner);
        String[] items = new String[]{"Сначало популярное","Оценки по убыванию", "Оценки по возрастанию", "Сложность по убыванию", "Сложность по возрастанию"};
        ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        category.setAdapter(category_adapter);

        rvMain = (RecyclerView) this.getActivity().findViewById(R.id.rvMain);
        Adaptery rvMain_adapter = new Adaptery(values, images);
        rvMain.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvMain.setAdapter(rvMain_adapter);

//        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
//        rvMain.addItemDecoration(itemDecor);
    }

}