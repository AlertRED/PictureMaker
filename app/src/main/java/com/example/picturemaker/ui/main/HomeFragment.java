package com.example.picturemaker.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.picturemaker.AdapterHomeTopRV;
import com.example.picturemaker.ImageHelper;
import com.example.picturemaker.R;
import com.example.picturemaker.TestData;

public class HomeFragment extends Fragment {

//    private MainViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    private void addView(int drawable){
        LinearLayout home_ll = (LinearLayout) this.getActivity().findViewById(R.id.home_ll);
        LayoutInflater inflater = getLayoutInflater();

        View layer1 = inflater.inflate(R.layout.pictute_item_popular, null);
        ImageView im = layer1.findViewById(R.id.imageview);
        im.setImageResource(drawable);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20,10, 20,10);
        layer1.setLayoutParams(params);
        home_ll.addView(layer1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.addView(R.drawable.image1);
        this.addView(R.drawable.image2);
        this.addView(R.drawable.image3);


        RecyclerView rv_last = (RecyclerView) this.getActivity().findViewById(R.id.rv_new);
        rv_last.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        AdapterHomeTopRV rvMain_adapter = new AdapterHomeTopRV(getContext(), TestData.names, TestData.pictures, R.layout.pictute_item_top, 0, 20);
//        rv_last.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rv_last.setAdapter(rvMain_adapter);
    }
}