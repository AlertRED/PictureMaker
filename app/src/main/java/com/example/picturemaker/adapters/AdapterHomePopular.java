package com.example.picturemaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.storage.Storage;

import java.util.ArrayList;
import java.util.List;

class ViewHolderHomePopular extends RecyclerView.ViewHolder {
    public ImageView image;
    private TextView title;
    private ImageView favorite;
    private View layer;

    public ViewHolderHomePopular(View view) {
        super(view);
        this.image = view.findViewById(R.id.imageview);
        this.title = view.findViewById(R.id.picture_name);
        this.favorite = view.findViewById(R.id.favorite_image_item_home);
        this.layer = view;
    }

    public View getLayer() {
        return layer;
    }

    public void setTitle(String text) {
        this.title.setText(text);
    }

    public ImageView getFavorite() {
        return favorite;
    }
}

public class AdapterHomePopular extends RecyclerView.Adapter<ViewHolderHomePopular> {
    int layout_item;
    int spacing_vertical;
    int spacing_horizontal;
    Context context;
    Storage storage;
    private List<Picture> pictures;

    public AdapterHomePopular(Context context, int layout_item, int spacing_vertical, int spacing_horizontal) {
        this.context = context;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.pictures = new ArrayList<>();
        this.storage = Storage.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolderHomePopular onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderHomePopular(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHomePopular holder, int position) {
        Picture picture = this.pictures.get(position);
        storage.GetImage(context, picture.public_picture, holder.image);

        holder.setTitle(picture.name);
        holder.getFavorite().setImageResource(picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
        holder.getFavorite().setOnClickListener(v -> storage.SetFavoritePicture(picture.id, !picture.is_favorite));

        holder.getLayer().setOnClickListener(v -> {
            Intent intent = new Intent(context, PictureActivity.class);
            intent.putExtra("picture_id", picture.id);
            context.startActivity(intent);
        });
    }

    public List<Picture> getData() {
        return this.pictures;
    }

    public void setData(List<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }
}
