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

import java.util.ArrayList;
import java.util.List;

class ViewHolderColorsRV extends RecyclerView.ViewHolder {

    private final TextView text;
    public View layer;

    public ViewHolderColorsRV(View itemView) {
        super(itemView);
        layer = itemView;
        text = itemView.findViewById(R.id.color_shape);
    }

    public void setBrush(Integer color, Integer n) {
        GradientDrawable drawable = (GradientDrawable) text.getBackground();
        drawable.setColor(color);
//        int R = color % 256;
//        int G = (color / 256) % 256;
//        int B = (color / 256 / 256) % 256;
//        if (R < 10 || G < 10 || B < 10)
//            this.text.setTextColor(Color.WHITE);
//        else
//            this.text.setTextColor(Color.BLACK);
        this.text.setText(String.valueOf(n));

    }
}

public class AdapterColorsRV extends RecyclerView.Adapter<ViewHolderColorsRV> {
    int layout_item;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;
    Context context;
    List<Integer> colors = new ArrayList<>();

    public AdapterColorsRV(Context context, int layout_item) {
        this.context = context;
        this.layout_item = layout_item;
    }


    public AdapterColorsRV(Context context, List<Integer> colors, int layout_item, int spacing_vertical, int spacing_horizontal) {
        this.context = context;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.colors = colors;
    }

    @Override
    public ViewHolderColorsRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderColorsRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderColorsRV holder, final int position) {

        Integer color = this.colors.get(position);
        holder.setBrush(color, position + 1);

        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && position < this.getItemCount() - 1) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(0, this.spacing_vertical, this.spacing_horizontal, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }
}
