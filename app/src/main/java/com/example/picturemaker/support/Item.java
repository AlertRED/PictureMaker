package com.example.picturemaker.support;

public class Item{

    public int picture;
    public String name;
    public boolean is_favorite;
    public int id;
    public int score;
    public int progress;
    public int total_score;
    public int level;

    public Item(int picture, String name, int id) {
        this.picture = picture;
        this.name = name;
        this.id = id;
        this.is_favorite = false;
        this.score = 0;
        this.level = 2;
    }
}
