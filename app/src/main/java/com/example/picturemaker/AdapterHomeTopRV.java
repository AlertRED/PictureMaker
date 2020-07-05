package com.example.picturemaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

class ViewHolderHomeTopRV extends RecyclerView.ViewHolder{
    public ImageView image;
    public TextView text;

    public ViewHolderHomeTopRV(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.imageview);
        text = (TextView)itemView.findViewById(R.id.textview);
    }
}

public class AdapterHomeTopRV extends RecyclerView.Adapter<ViewHolderHomeTopRV> {

    String[] texts;
    int[] images;
    boolean[] is_favorites;
    int layout_item;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;
    Context context;

    public AdapterHomeTopRV(Context context, String[] texts, int[] images, int layout_item) {
        this.context = context;
        this.texts = texts;
        this.images = images;
        this.layout_item = layout_item;
        this.is_favorites  = new boolean[texts.length];
        Arrays.fill(this.is_favorites, false);
    }

    public AdapterHomeTopRV(Context context, String[] texts, int[] images, int layout_item, int spacing_vertical, int spacing_horizontal) {
        this.context = context;
        this.texts = texts;
        this.images = images;
        this.layout_item = layout_item;
        this.is_favorites  = new boolean[texts.length];
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        Arrays.fill(this.is_favorites, false);
    }

    @Override
    public ViewHolderHomeTopRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderHomeTopRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderHomeTopRV holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), texts[position], Toast.LENGTH_SHORT).show();
            }
        });

        Bitmap bm = (Bitmap) BitmapFactory.decodeResource(context.getResources(), images[position]);
        holder.image.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bm,10));
        holder.text.setText(this.texts[position]);

        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && position < this.getItemCount()-1){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(0, this.spacing_vertical, this.spacing_horizontal, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return texts.length;
    }
}
