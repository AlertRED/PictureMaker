package com.example.picturemaker.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;

@Dao
interface PictureDao {
    @Query("SELECT * FROM Picture")
    LiveData<List<Picture>> getAll();

    @Query("SELECT * FROM Picture WHERE public_id IN (:userIds)")
    List<Picture> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Picture WHERE name LIKE :name LIMIT 1")
    Picture findByName(String name);

    @Query("SELECT * FROM Picture WHERE public_id IS :id")
    Maybe<Picture> findById(String id);

    @Insert
    void insertAll(Picture... pictures);

    @Delete
    void delete(Picture picture);

    @Update
    void update(Picture picture);
}

@Database(entities = {Picture.class}, version = 1, exportSchema = false)
public abstract class InternalDB extends RoomDatabase {
    public abstract PictureDao pictureDao();
}