package com.example.picturemaker.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

@Dao
interface ViewPictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ViewPicture viewPicture);

    @Query("SELECT * FROM view_picture")
    List<ViewPicture> getAll();

    @Query("SELECT * FROM view_picture WHERE view_picture.viewName=:viewName")
    List<ViewPicture> getFromView(String viewName);

    @Query("SELECT picture.* FROM picture INNER JOIN view_picture ON view_picture.viewName=:viewName AND view_picture.pictureId=picture.id  GROUP BY picture.id")
    LiveData<List<Picture>> getPicturesFromView(final String viewName);

    @Query("DELETE FROM view_picture WHERE view_picture.viewName=:viewName")
    void deleteAllPicturesFromView(final String viewName);
}

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Picture picture);

    @Query("SELECT * FROM Picture")
    LiveData<List<Picture>> liveAll();

    @Query("SELECT * FROM Picture")
    List<Picture> getAll();

    @Query("SELECT * FROM Picture WHERE public_id IN (:pictureIds)")
    LiveData<List<Picture>> loadAllByIds(int[] pictureIds);

    @Query("SELECT * FROM Picture WHERE name LIKE :name LIMIT 1")
    Picture findByName(String name);

    @Query("SELECT * FROM Picture WHERE id IS :id")
    Picture findById(long id);

    @Query("SELECT * FROM Picture WHERE id IS :id")
    LiveData<Picture> liveById(long id);

    @Query("SELECT * FROM Picture WHERE public_id IS :public_id")
    Picture findByPublicId(String public_id);

    @Query("SELECT picture.id FROM Picture WHERE public_id IS :public_id")
    long getIdByPublicId(String public_id);

//    @Insert
//    void insertAll(Picture... pictures);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Picture> pictures);

    @Delete
    void delete(Picture picture);

    @Query("DELETE FROM picture")
    void deleteAll();

    @Query("UPDATE picture SET name = :name AND level=:level AND total_score=:total_score AND public_picture=:public_picture AND is_favorite=:is_favorite AND score=:score AND progress=:progress WHERE id = :id")
    int updatePicture(long id, String name, int level, int total_score, String public_picture, boolean is_favorite, int score, int progress);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Picture picture);
}

@Database(entities = {Picture.class, ViewPicture.class}, version = 22, exportSchema = false)
public abstract class InternalDB extends RoomDatabase {
    public abstract PictureDao pictureDao();

    public abstract ViewPictureDao viewPictureDao();
}