package com.example.picturemaker;

public class Item{

    public int picture;
    public String name;
    public boolean is_favorite;
    public int id;
    public int score;
    public int progress;

    public Item(int picture, String name, int id) {
        this.picture = picture;
        this.name = name;
        this.id = id;
        this.is_favorite = false;
        this.score = 0;
    }
}
