package com.example.picturemaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.storage.Storage;

import java.util.ArrayList;
import java.util.List;

class ViewHolderHomeNews extends RecyclerView.ViewHolder {

    public ImageView image;
    private TextView text;
    private View layer;


    public ViewHolderHomeNews(View itemView) {
        super(itemView);
        layer = itemView;
        image = (ImageView) itemView.findViewById(R.id.imageview);
        text = (TextView) itemView.findViewById(R.id.picture_name);
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

    public View getLayer() {
        return layer;
    }
}

public class AdapterHomeNews extends RecyclerView.Adapter<ViewHolderHomeNews> {

    int layout_item;
    int spacing_vertical;
    int spacing_horizontal;
    Context context;
    Storage storage;
    private List<Picture> pictures;

    public AdapterHomeNews(Context context, int layout_item, int spacing_vertical, int spacing_horizontal) {
        this.context = context;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.pictures = new ArrayList<>();
        this.storage = Storage.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolderHomeNews onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderHomeNews(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderHomeNews holder, final int picture_id) {

        Picture picture = pictures.get(picture_id);
        storage.GetImage(context, picture.public_picture, holder.image);

        holder.itemView.setOnClickListener(v -> Toast.makeText(v.getContext(), picture.name, Toast.LENGTH_SHORT).show());

        holder.getLayer().setOnClickListener(v -> {
            Intent intent = new Intent(context, PictureActivity.class);
            intent.putExtra("picture_id", picture.id);
            context.startActivity(intent);
        });

        holder.getText().setText(picture.name);
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
