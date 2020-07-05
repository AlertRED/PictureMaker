package com.example.picturemaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;


class ViewHolderGalleryRV extends RecyclerView.ViewHolder{
    public ImageView image;
    public TextView text;
    public ImageButton favorite;

    public ViewHolderGalleryRV(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.imageview);
        text = (TextView)itemView.findViewById(R.id.textview);
        favorite = (ImageButton) itemView.findViewById(R.id.favorite_button_item2);
    }
}

public class AdapterGalleryRV extends RecyclerView.Adapter<ViewHolderGalleryRV> {

    String[] texts;
    int[] images;
    boolean[] is_favorites;
    int layout_item;
    boolean first;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;

    public AdapterGalleryRV(String[] texts, int[] images, int layout_item) {
        this.texts = texts;
        this.images = images;
        this.layout_item = layout_item;
        this.is_favorites  = new boolean[texts.length];
        Arrays.fill(this.is_favorites, false);
    }

    public AdapterGalleryRV(String[] texts, int[] images, int layout_item, int spacing_vertical, int spacing_horizontal, boolean first) {
        this.first = first;
        this.texts = texts;
        this.images = images;
        this.layout_item = layout_item;
        this.is_favorites  = new boolean[texts.length];
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        Arrays.fill(this.is_favorites, false);
    }

    @Override
    public ViewHolderGalleryRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderGalleryRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderGalleryRV holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), texts[position], Toast.LENGTH_SHORT).show();
            }
        });
        holder.image.setImageResource(images[position]);
        holder.text.setText(texts[position]);
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_favorites[position]){
                    holder.favorite.setImageResource(R.drawable.ic_favorite_36x36);
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_unfavorite_36x36);
                }
                is_favorites[position] = !is_favorites[position];
            }
        });

        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && (!this.first || position > 0)){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(this.spacing_horizontal, this.spacing_vertical, 0, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return texts.length;
    }
}
