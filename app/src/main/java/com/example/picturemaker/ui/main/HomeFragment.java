package com.example.picturemaker.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.FirebaseDB;
import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.adapters.AdapterHomeTopRV;
import com.example.picturemaker.support.Item;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

class HomeHolder {
    private ImageView image;
    private TextView title;
    private ImageView favorite;
    private View layer;

    public HomeHolder(View view) {
        this.image = view.findViewById(R.id.imageview);
        this.title = view.findViewById(R.id.picture_name);
        this.favorite = view.findViewById(R.id.favorite_image_item_home);
        this.layer = view;
        this.layer.setVisibility(View.GONE);
    }

    public ImageView getImage() {
        return image;
    }

    private void setImage(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
        this.layer.setVisibility(View.VISIBLE);
    }

    public void loadImage(Context context, String name) {
        FirebaseDB.loadPicture(context, name, this::setImage, false);
    }

    public View getLayer() {
        return layer;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public ImageView getFavorite() {
        return favorite;
    }

    public void setFavorite(ImageView favorite) {
        this.favorite = favorite;
    }

}

public class HomeFragment extends Fragment {

    private Map<Item, View> layers = new Hashtable<>();
    private RecyclerView rv_top;

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
        for (Map.Entry<Item, View> entry : layers.entrySet()) {
            final Item item = entry.getKey();

            HomeHolder holder = new HomeHolder(entry.getValue());
            holder.loadImage(this.getContext(), item.public_picture);
            holder.getFavorite().setImageResource(item.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
            holder.getTitle().setText(item.name);

            holder.getFavorite().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.is_favorite) {
                        holder.getFavorite().setImageResource(R.drawable.ic_favorite_36);
                        Toast.makeText(v.getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.getFavorite().setImageResource(R.drawable.ic_unfavorite_36);
                        Toast.makeText(v.getContext(), "Убрано из избранного", Toast.LENGTH_SHORT).show();
                    }
                    item.is_favorite = !item.is_favorite;
                }
            });

            holder.getLayer().setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                    intent.putExtra("picture_id", item.public_id);
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
        this.layers.put(item, layer);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20, 10, 20, 10);
        layer.setLayoutParams(params);
        home_ll.addView(layer);
    }

    public void addView(List<Item> items) {
        for (Item item : items)
            this.addView(item);
        this.RefreshData();
    }

    public void RefreshAdapter(List<Item> items) {
        AdapterHomeTopRV rvMain_adapter = new AdapterHomeTopRV(getContext(), R.layout.item_pictute_top, items, 0, 20);
        rv_top.setAdapter(rvMain_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseDB.loadItem(this::addView);

        rv_top = (RecyclerView) this.getActivity().findViewById(R.id.rv_new);
        rv_top.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        FirebaseDB.loadItem(this::RefreshAdapter);
    }
}