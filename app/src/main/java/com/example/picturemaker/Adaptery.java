package com.example.picturemaker;

import android.content.Context;
import android.util.Log;
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

    public Adaptery(Context mContext, String[] values, int[] images) {
        this.mContext = mContext;
        this.values = values;
        this.images = images;
        for (String s: values) {
            Log.i("", s);
        }
    }

    @Override
    public int getCount(){
        return values.length;
    }

    public Object getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if  (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_gallery, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            TextView textView = (TextView) view.findViewById(R.id.textview);
            imageView.setImageResource(images[i]);
            textView.setText(values[i]);
        }
        return view;
    }
}
