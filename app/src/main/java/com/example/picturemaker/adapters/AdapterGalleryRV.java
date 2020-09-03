package com.example.picturemaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


class ViewHolderGalleryRV extends RecyclerView.ViewHolder {
    public TextView text;
    public ImageView favorite;
    public View layer;
    public ImageView image;


    public ViewHolderGalleryRV(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.imageview);
        text = (TextView) itemView.findViewById(R.id.picture_name);
        favorite = (ImageView) itemView.findViewById(R.id.favorite_image_item_gallery);
        layer = itemView;
    }

}

public class AdapterGalleryRV extends RecyclerView.Adapter<ViewHolderGalleryRV> {
    private final Resources resources;
    Context context;
    int layout_item;
    boolean first;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;
    List<Picture> pictures;
    Storage storage;


    public AdapterGalleryRV(Context context, Resources resources, int layout_item, int spacing_vertical, int spacing_horizontal, boolean first) {
        this.context = context;
        this.first = first;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.pictures = new ArrayList<>();
        this.storage = Storage.getInstance(context);
        this.pictures = new ArrayList<>();
        this.resources = resources;
    }


    @NonNull
    @Override
    public ViewHolderGalleryRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderGalleryRV(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolderGalleryRV holder, final int position) {
        Picture picture = this.pictures.get(position);
        storage.GetImage(context, picture.public_picture, holder.image);

        holder.text.setText(picture.name);
        holder.favorite.setImageResource(picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
        holder.favorite.setOnClickListener(v -> storage.SetFavoritePicture(picture.id, !picture.is_favorite));

        holder.layer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PictureActivity.class);
            intent.putExtra("pictureId", picture.id);
            context.startActivity(intent);
        });

        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && (!this.first || position > 0)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(this.spacing_horizontal, this.spacing_vertical, 0, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    public List<Picture> getData() {
        return this.pictures;
    }

    public void setData(List<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public int getItemCount() {
        return this.pictures.size();
    }

}
