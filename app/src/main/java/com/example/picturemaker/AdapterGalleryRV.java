package com.example.picturemaker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    public ImageView favorite;
    public View layer;

    public ViewHolderGalleryRV(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.imageview);
        text = (TextView)itemView.findViewById(R.id.textview);
        favorite = (ImageView) itemView.findViewById(R.id.favorite_image_item_gallery);
        layer = itemView;
    }
}

public class AdapterGalleryRV extends RecyclerView.Adapter<ViewHolderGalleryRV> {

    Context context;
    int layout_item;
    boolean first;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;

    public AdapterGalleryRV(Context context, int layout_item) {
        this.context = context;
        this.layout_item = layout_item;
    }

    public AdapterGalleryRV(Context context, int layout_item, int spacing_vertical, int spacing_horizontal, boolean first) {
        this.context = context;
        this.first = first;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
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
                Toast.makeText(v.getContext(), TestData.get(position).name, Toast.LENGTH_SHORT).show();
            }
        });
        holder.image.setImageResource(TestData.get(position).picture);
        holder.text.setText(TestData.get(position).name);

        if (TestData.get(position).is_favorite)
            holder.favorite.setImageResource(R.drawable.ic_favorite_36x36);
        else holder.favorite.setImageResource(R.drawable.ic_unfavorite_36x36);

        holder.layer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityPicture.class);
                intent.putExtra("picture_id", TestData.get(position).id);
                context.startActivity(intent);
            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TestData.get(position).is_favorite){
                    holder.favorite.setImageResource(R.drawable.ic_favorite_36x36);
                    Toast.makeText(v.getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_unfavorite_36x36);
                    Toast.makeText(v.getContext(), "Убрано из избранного", Toast.LENGTH_SHORT).show();

                }
                TestData.get(position).is_favorite = !TestData.get(position).is_favorite;
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
        return TestData.size();
    }
}
