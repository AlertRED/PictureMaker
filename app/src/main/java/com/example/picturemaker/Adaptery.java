package com.example.picturemaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

class MyViewHolder extends RecyclerView.ViewHolder{
    public ImageView image;
    public TextView text;
    public ImageButton favorite;

    public MyViewHolder(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.imageview);
        text = (TextView)itemView.findViewById(R.id.textview);
        favorite = (ImageButton) itemView.findViewById(R.id.favorite_button_item);
    }
}

public class Adaptery extends RecyclerView.Adapter<MyViewHolder> {

    String[] texts;
    int[] images;
    boolean[] is_favorites;

    public Adaptery(String[] texts, int[] images) {
        this.texts = texts;
        this.images = images;
        this.is_favorites  = new boolean[texts.length];
        Arrays.fill(this.is_favorites, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
//        if (position>0){
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 20, 0, 0);
//            holder.itemView.setLayoutParams(params);
//        }
    }

    @Override
    public int getItemCount() {
        return texts.length;
    }
}
