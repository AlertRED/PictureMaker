package com.example.picturemaker.Storage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Picture {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "public_id")
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

    public Picture(@NonNull String public_id) {
        this.public_id = public_id;
    }
}
