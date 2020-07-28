package com.example.picturemaker.Storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Picture {
    @PrimaryKey
    public String public_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "level")
    public int level;

    @ColumnInfo(name = "total_score")
    public int total_score;

    @ColumnInfo(name = "public_picture")
    public String public_picture;

    @ColumnInfo(name = "is_favorite")
    public boolean is_favorite;

    @ColumnInfo(name = "score")
    public int score;

    @ColumnInfo(name = "progress")
    public int progress;

    public Picture() {
    }

    public Picture(String name, String public_picture, int level) {
        this.public_picture = public_picture;
        this.name = name;
        this.level = level;
    }
}
