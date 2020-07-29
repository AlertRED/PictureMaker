package com.example.picturemaker.Storage;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import java.util.List;

@Dao
interface PictureDao {
    @Query("SELECT * FROM Picture")
    List<Picture> getAll();

    @Query("SELECT * FROM Picture WHERE public_id IN (:userIds)")
    List<Picture> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Picture WHERE name LIKE :name LIMIT 1")
    Picture findByName(String name);

    @Query("SELECT * FROM Picture WHERE public_id LIKE :id LIMIT 1")
    Picture findById(String id);

    @Insert
    void insertAll(Picture... pictures);

    @Delete
    void delete(Picture picture);
}

@Database(entities = {Picture.class}, version = 1)
public abstract class InternalDB extends RoomDatabase {
    public abstract PictureDao pictureDao();
}