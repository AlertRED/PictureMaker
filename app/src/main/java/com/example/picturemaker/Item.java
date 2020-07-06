package com.example.picturemaker;

public class Item{

    public int picture;
    public String name;
    public boolean is_favorite;
    public int id;


    public Item(int picture, String name, int id,boolean is_favorite) {
        this.picture = picture;
        this.name = name;
        this.id = id;
        this.is_favorite = is_favorite;
    }
}
