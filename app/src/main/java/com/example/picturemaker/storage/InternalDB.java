package com.example.picturemaker.storage;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.storage.room_tables.ViewPicture;

import java.util.List;

@Dao
interface ViewPictureDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ViewPicture viewPicture);

    @Query("DELETE FROM view_picture WHERE viewName IS :viewName AND pictureId=:pictureId")
    void deleteByViewAndPicture(String viewName, long pictureId);

    @Query("SELECT picture.* FROM picture INNER JOIN view_picture ON view_picture.viewName=:viewName AND view_picture.pictureId=picture.id ORDER BY view_picture.id")
    LiveData<List<Picture>> getPicturesFromView(final String viewName);

    @Query("DELETE FROM view_picture WHERE view_picture.viewName=:viewName")
    void deleteAllPicturesFromView(final String viewName);

    @Query("DELETE FROM view_picture")
    void deleteAll();
}

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertIgnore(Picture picture);

    @Update
    void update(Picture picture);

    @Transaction
    default boolean is_new(Picture picture) {
        return findByPublicId(picture.public_id) == null;
    }

    @Transaction
    default long insertOrUpdate(Picture picture) {
        long id = insertIgnore(picture);
        if (id == -1) {
            update(picture);
            id = findByPublicId(picture.public_id).id;
        }
        return id;
    }

    @Query("SELECT * FROM Picture WHERE id IS :id")
    Picture findById(long id);

    @Query("SELECT * FROM Picture WHERE id IS :id")
    LiveData<Picture> liveById(long id);

    @Query("SELECT * FROM Picture WHERE public_id IS :public_id")
    Picture findByPublicId(String public_id);
}

@Database(entities = {Picture.class, ViewPicture.class}, version = 37, exportSchema = false)
public abstract class InternalDB extends RoomDatabase {

    private static volatile InternalDB INSTANCE;

    static InternalDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (InternalDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, InternalDB.class, "database-name").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract PictureDao pictureDao();

    public abstract ViewPictureDao viewPictureDao();
}