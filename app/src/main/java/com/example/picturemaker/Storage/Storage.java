package com.example.picturemaker.Storage;

import android.content.Context;

import androidx.room.Room;

import java.util.List;
import androidx.core.util.Consumer;

public class Storage {

    LocalStorage localStorage;
    InternalDB db;
    ItemDao pictureDao;

    public Storage(Context context) {
        localStorage = new LocalStorage();
        db = Room.databaseBuilder(context, InternalDB.class, "database-name").build();
        pictureDao = db.userDao();
    }

    public void GetItems(Consumer<List<Picture>> foo, boolean is_favorite, int level) {
        List<Picture> pictures = this.localStorage.Query(is_favorite, level);
        if (pictures == null) {
            FirebaseDB.loadItems(pictures1 -> {
                this.localStorage.SaveStorage(pictures1, is_favorite, level);
                foo.accept(pictures1);
            });
        } else {
            foo.accept(pictures);
        }
    }

    public void GetItem(Consumer<Picture> foo, String picture_id){
        Picture picture = this.pictureDao.findById(picture_id);
        if (picture == null) {
            picture = localStorage.GetPicture(picture_id);
        }
        if (picture == null) {
            FirebaseDB.loadItem(picture1 -> {
                this.localStorage.SaveStorage(picture1);
                foo.accept(picture1);
            },picture_id);
        } else {
            foo.accept(picture);
        }
    }
}
