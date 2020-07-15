package com.example.picturemaker.support;

import android.graphics.Bitmap;

public class Item {
    public String public_id;
    public String name;
    public int level;
    public int total_score;
    public String public_picture;


    public Bitmap pictureBits;

    public boolean is_favorite;
    public int score;
    public int progress;

    public Item() {
    }

    public Item(String name, String public_picture, int level) {
        this.public_picture = public_picture;
        this.name = name;
        this.level = level;
    }

//    public Item(int picture, String name, int id) {
//        this.picture = picture;
//        this.name = name;
//        this.id = id;
//        this.is_favorite = false;
//        this.score = 0;
//        this.level = 2;
//    }
}
