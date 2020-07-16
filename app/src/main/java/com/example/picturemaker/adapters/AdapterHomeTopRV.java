package com.example.picturemaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.Firebase;
import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.support.Item;
import com.example.picturemaker.support.TestData;

import java.util.ArrayList;
import java.util.List;

class ViewHolderHomeTopRV extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView text;
    private View layer;

    public ViewHolderHomeTopRV(View itemView) {
        super(itemView);
        layer = itemView;
        image = (ImageView) itemView.findViewById(R.id.imageview);
        text = (TextView) itemView.findViewById(R.id.picture_name);
        this.layer.setVisibility(View.GONE);
    }

    public void loadImage(String name) {
        Firebase.loadPicture(name, this::setImage);
    }

    private void setImage(Bitmap bitmap){
        this.image.setImageBitmap(bitmap);
        this.layer.setVisibility(View.VISIBLE);
    }

    public void setText(TextView text) {
        this.text = text;
    }

    public void setLayer(View layer) {
        this.layer = layer;
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getText() {
        return text;
    }

    public View getLayer() {
        return layer;
    }
}

public class AdapterHomeTopRV extends RecyclerView.Adapter<ViewHolderHomeTopRV> {

    private List<Item> items = new ArrayList<>();
    int layout_item;
    int spacing_vertical = 0;
    int spacing_horizontal = 0;
    Context context;

    public AdapterHomeTopRV(Context context, int layout_item) {
        this.context = context;
        this.layout_item = layout_item;
    }

    public AdapterHomeTopRV(Context context, int layout_item, List<Item> items, int spacing_vertical, int spacing_horizontal) {
        this.context = context;
        this.layout_item = layout_item;
        this.spacing_horizontal = spacing_horizontal;
        this.spacing_vertical = spacing_vertical;
        this.items = items;
    }

    @Override
    public ViewHolderHomeTopRV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout_item, parent, false);
        return new ViewHolderHomeTopRV(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderHomeTopRV holder, final int position) {

        Item item = this.items.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), item.name, Toast.LENGTH_SHORT).show();
            }
        });

        holder.getLayer().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
//                intent.putExtra("picture_id", TestData.get(position).id);
                context.startActivity(intent);
            }
        });

        holder.getText().setText(item.name);

        holder.loadImage(item.public_picture);

        if ((this.spacing_horizontal > 0 || this.spacing_vertical > 0) && position < this.getItemCount() - 1) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams().width, holder.itemView.getLayoutParams().height);
            params.setMargins(0, this.spacing_vertical, this.spacing_horizontal, 0);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
