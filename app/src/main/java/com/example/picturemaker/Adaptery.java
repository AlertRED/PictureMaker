package com.example.picturemaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


class MyViewHolder extends RecyclerView.ViewHolder{
    public ImageView image;
    public TextView text;

    public MyViewHolder(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.imageview);
        text = (TextView)itemView.findViewById(R.id.textview);
    }
}

public class Adaptery extends RecyclerView.Adapter<MyViewHolder> {

    String[] texts;
    int[] images;

    public Adaptery(String[] texts, int[] logoList) {
        this.texts = texts;
        this.images = logoList;
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
        if (position>0){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return texts.length;
    }
}
