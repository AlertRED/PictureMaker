package com.example.picturemaker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.R;

import java.util.Random;

class ViewHolderColorsRV extends RecyclerView.ViewHolder {

    private final TextView text;
    public View layer;

    public ViewHolderColorsRV(View itemView) {
        super(itemView);
        layer = itemView;
        text = itemView.findViewById(R.id.color_shape);
        Random rnd = new Random();

        text.setText(String.valueOf(rnd.nextInt(99)));

        GradientDrawable drawable = (GradientDrawable) text.getBackground();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        drawable.setColor(color);
    }
}

public class AdapterColorsRV extends RecyclerView.Adapter<ViewHolderColorsRV> {
    int layout_item;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;
    Context context;

    public AdapterColorsRV(Context context, int layout_item) {
        this.context = context;
        this.layout_item = layout_item;
    }

    public AdapterColorsRV(Context context, int layout_item, int spacing_vertical, int spacing_horizontal) {
        this.context = context;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
    }

    @Override
    public ViewHolderColorsRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderColorsRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderColorsRV holder, final int position) {

        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && position < this.getItemCount() - 1) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(0, this.spacing_vertical, this.spacing_horizontal, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
