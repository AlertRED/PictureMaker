package com.example.picturemaker.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.adapters.AdapterHomeTopRV;
import com.example.picturemaker.support.Item;
import com.example.picturemaker.support.TestData;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class HomeFragment extends Fragment {

    private Map<Integer, View> layers = new Hashtable<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void RefreshData() {
        for (Map.Entry<Integer, View> entry : layers.entrySet()) {
            Integer key = entry.getKey();
            View value = entry.getValue();

            final Item item = TestData.get_id(key);
            ImageView picture = value.findViewById(R.id.imageview);
            final ImageView favorite = value.findViewById(R.id.favorite_image_item_home);

            picture.setImageResource(item.picture);
            favorite.setImageResource(item.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.is_favorite) {
                        favorite.setImageResource(R.drawable.ic_favorite_36);
                        Toast.makeText(v.getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                    } else {
                        favorite.setImageResource(R.drawable.ic_unfavorite_36);
                        Toast.makeText(v.getContext(), "Убрано из избранного", Toast.LENGTH_SHORT).show();
                    }
                    item.is_favorite = !item.is_favorite;
                }
            });

            value.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                    intent.putExtra("picture_id", item.id);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.RefreshData();
    }

    private void addView(final Item item) {
        LinearLayout home_ll = (LinearLayout) this.getActivity().findViewById(R.id.home_ll);
        LayoutInflater inflater = getLayoutInflater();

        View layer = inflater.inflate(R.layout.item_pictute_popular, null);
        this.layers.put(item.id, layer);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20, 10, 20, 10);
        layer.setLayoutParams(params);
        home_ll.addView(layer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Iterator<Item> iter = TestData.getIter();
        while (iter.hasNext()) {
            this.addView(iter.next());
        }

        RecyclerView rv_top = (RecyclerView) this.getActivity().findViewById(R.id.rv_new);
        rv_top.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        AdapterHomeTopRV rvMain_adapter = new AdapterHomeTopRV(getContext(), R.layout.item_pictute_top, 0, 20);
        rv_top.setAdapter(rvMain_adapter);
    }
}