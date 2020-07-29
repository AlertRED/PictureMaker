package com.example.picturemaker.Storage;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.room.Room;

import java.util.List;
import androidx.core.util.Consumer;

public class Storage {

    LocalStorage localStorage;
    InternalDB db;
    PictureDao pictureDao;
    Context context;

    private static Storage instance;

    private Storage(Context context) {
        this.localStorage = new LocalStorage();
        this.db = Room.databaseBuilder(context, InternalDB.class, "database-name").build();
        this.pictureDao = this.db.pictureDao();
        this.context = context;
    }

    public static Storage getInstance(Context context){
        if(instance == null){
            instance = new Storage(context);
        }
        return instance;
    }

    public void GetPictures(Consumer<List<Picture>> foo, boolean is_favorite, int level) {
        List<Picture> pictures = this.localStorage.Query(is_favorite, level);
        if (pictures != null) {
            foo.accept(pictures);
        } else {
            FirebaseDB.loadItems(pictures1 -> {
                this.localStorage.SaveStorage(pictures1, is_favorite, level);
                foo.accept(pictures1);
            });
        }
    }

    public void GetImage(Context context, String picture_id, Consumer<Bitmap> foo) {
        FirebaseDB.loadPicture(context, picture_id,foo, false);
    }



    public void GetPicture(Consumer<Picture> foo, String picture_id){
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
