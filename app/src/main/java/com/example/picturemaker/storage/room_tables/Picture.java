package com.example.picturemaker.storage.room_tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"public_id"}, unique = true)})
public class Picture {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "public_id")
    public String public_id;

    @ColumnInfo(name = "name_id")
    public Long name_id;

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

    public Picture(int id, String public_id, Long name_id, String name, int level, int total_score, String public_picture, boolean is_favorite, int score, int progress) {
        this.id = id;
        this.public_id = public_id;
        this.name_id = name_id;
        this.name = name;
        this.level = level;
        this.total_score = total_score;
        this.public_picture = public_picture;
        this.is_favorite = is_favorite;
        this.score = score;
        this.progress = progress;
    }
}
