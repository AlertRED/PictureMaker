package com.example.picturemaker.storage;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "view_picture",
        foreignKeys = {@ForeignKey(entity = Picture.class,
                        parentColumns = "id",
                        childColumns = "pictureId")
        },indices = {@Index(value = {"viewName", "pictureId"},
        unique = true)})

public class ViewPicture {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public final String viewName;
    public final long pictureId;

    public ViewPicture(String viewName, long pictureId) {
        this.viewName = viewName;
        this.pictureId = pictureId;
    }
}
