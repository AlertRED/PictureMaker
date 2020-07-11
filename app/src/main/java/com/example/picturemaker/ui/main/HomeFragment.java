package com.example.picturemaker.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picturemaker.ActivityPicture;
import com.example.picturemaker.AdapterHomeTopRV;
import com.example.picturemaker.ImageHelper;
import com.example.picturemaker.Item;
import com.example.picturemaker.R;
import com.example.picturemaker.TestData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    private void addView(final Item item){
        LinearLayout home_ll = (LinearLayout) this.getActivity().findViewById(R.id.home_ll);
        LayoutInflater inflater = getLayoutInflater();

        View layer = inflater.inflate(R.layout.pictute_item_popular, null);

        layer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityPicture.class);
                intent.putExtra("picture_id", item.id);
                startActivity(intent);
            }
        });

        final ImageView image = layer.findViewById(R.id.imageview);
        image.setImageResource(item.picture);

        final ImageView favorite = layer.findViewById(R.id.favorite_image_item_home);

        if (item.is_favorite)
            favorite.setImageResource(R.drawable.ic_favorite_36x36);
        else favorite.setImageResource(R.drawable.ic_unfavorite_36x36);


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.is_favorite){
                    favorite.setImageResource(R.drawable.ic_favorite_36x36);
                    Toast.makeText(v.getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    favorite.setImageResource(R.drawable.ic_unfavorite_36x36);
                    Toast.makeText(v.getContext(), "Убрано из избранного", Toast.LENGTH_SHORT).show();
                }
                item.is_favorite = !item.is_favorite;
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20,10, 20,10);
        layer.setLayoutParams(params);
        home_ll.addView(layer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Iterator<Item> iter = TestData.getIter();
        while(iter.hasNext()){
            this.addView(iter.next());
        }

        RecyclerView rv_top = (RecyclerView) this.getActivity().findViewById(R.id.rv_new);
        rv_top.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        AdapterHomeTopRV rvMain_adapter = new AdapterHomeTopRV(getContext(), R.layout.pictute_item_top, 0, 20);
        rv_top.setAdapter(rvMain_adapter);
    }
}