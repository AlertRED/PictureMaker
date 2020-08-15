package com.example.picturemaker.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.R;
import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;

import java.util.ArrayList;
import java.util.List;

class ViewHolderHomePopular extends RecyclerView.ViewHolder {
    private ImageView image;
    private TextView title;
    private ImageView favorite;
    private View layer;

    public ViewHolderHomePopular(View view) {
        super(view);
        this.image = view.findViewById(R.id.imageview);
        this.title = view.findViewById(R.id.picture_name);
        this.favorite = view.findViewById(R.id.favorite_image_item_home);
        this.layer = view;
//        this.layer.setAlpha(0);
    }

    public ImageView getImage() {
        return image;
    }

    private void setImage(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
//        this.layer.animate().alpha(1f).setDuration(250);
    }

    public void loadImage(Context context, String name) {
        Storage.getInstance(context).GetImage(context, name, this::setImage);
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
public class AdapterHomePopular extends RecyclerView.Adapter<ViewHolderHomePopular>{
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

    }

    public void setData(List<Picture> pictures){
        this.pictures = pictures;
    }

    public List<Picture> getData(){
        return this.pictures;
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }
}
