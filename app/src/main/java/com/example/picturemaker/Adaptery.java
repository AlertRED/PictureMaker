package com.example.picturemaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Adaptery extends BaseAdapter {

    private Context mContext;
    private final String [] values;
    private final int [] images;
    View view;
    LayoutInflater layoutInflater;

    public Adaptery(Context mContext, String[] values, int[] images) {
        this.mContext = mContext;
        this.values = values;
        this.images = images;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if  (view == null) {
//            view = new View(mContext);
            view = layoutInflater.inflate(R.layout.item_gallery, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            TextView textView = (TextView) view.findViewById(R.id.textview);
            imageView.setImageResource(images[i]);
            textView.setText(values[i]);
        }
        return view;
    }
}
