package com.example.picturemaker.storage;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Storage {

    private static Storage instance;
    public ViewPictureDao viewPictureDao;
    LocalStorage localStorage;
    InternalDB db;
    PictureDao pictureDao;
    Context context;

    private Storage(Context context) {
        this.localStorage = new LocalStorage();
        this.db = Room.databaseBuilder(context, InternalDB.class, "database-name").fallbackToDestructiveMigration().build();
        this.pictureDao = this.db.pictureDao();
        this.viewPictureDao = this.db.viewPictureDao();
        this.context = context;
    }

    public static Storage getInstance(Context context) {
        if (instance == null) {
            instance = new Storage(context);
        }
        return instance;
    }

    public void GetGenres(Consumer<List<String>> foo) {
        List<String> genres = this.localStorage.GetGenres();
        if (genres != null) {
            foo.accept(genres);
        } else {
            FirebaseDB.loadGenres(genres1 -> {
                this.localStorage.SaveStorageGenres(genres1);
                GetGenres(foo);
            });
        }
    }

    public void GetLevels(Consumer<List<String>> foo) {
        List<String> levels = this.localStorage.GetLevels();
        foo.accept(levels);
    }

    public void GetAuthors(Consumer<List<String>> foo) {
        List<String> authors = this.localStorage.GetAuthors();
        if (authors != null) {
            foo.accept(authors);
        } else {
            FirebaseDB.loadAuthors(authors1 -> {
                this.localStorage.SaveStorageAuthors(authors1);
                GetAuthors(foo);
            });
        }
    }

    public LiveData<List<Picture>> GetLiveDataFromView(String viewName) {
        return this.viewPictureDao.getPicturesFromView(viewName);
    }

    public void LoadPicturesByGallery(Map<String, Object> parameters) {
        this.DeletePictures("Gallery");
        this.LoadPictures("Gallery", parameters);
    }

    public void LoadPicturesByCollection() {
        Map<String, Object> parameters = new Hashtable<>();
        this.LoadPictures("Collection", parameters);
    }

    public void LoadPicturesByTop() {
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("is_popular", true);
        this.LoadPictures("Top", parameters);
    }

    public void LoadPicturesByPopular() {
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("is_popular", true);
        this.LoadPictures("Popular", parameters);
    }

    private long InsertOrUpdate(Picture picture) {
        long id = this.pictureDao.getIdByPublicId(picture.public_id);
        if (id == 0)
            return this.pictureDao.insert(picture);
        this.pictureDao.update(picture);
//        this.pictureDao.updatePicture(id, picture.name, picture.level, picture.total_score, "1.jpg", picture.is_favorite, picture.score, picture.progress);
        return id;
    }

    public LiveData<Picture> GetLivePicture(long id) {
        return this.pictureDao.liveById(id);
    }

    private void DeletePictures(String viewName) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> this.viewPictureDao.deleteAllPicturesFromView(viewName));
    }

    private void LoadPictures(String viewName, Map<String, Object> parameters) {
        FirebaseDB.LoadPictures(pictures -> {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                for (Picture picture : pictures) {
                    long id = InsertOrUpdate(picture);
                    this.viewPictureDao.insert(new ViewPicture(viewName, id));
                }
            });
        }, parameters);
    }

    public void GetImage(Context context, String picture_name, Consumer<Bitmap> foo) {
        FirebaseDB.loadImage(context, picture_name, foo, false);
    }

    public void SetFavoritePicture(long pictureId, boolean isFavorite) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            Picture picture = pictureDao.findById(pictureId);
            picture.is_favorite = isFavorite;
            pictureDao.update(picture);
            FirebaseDB.SetFavoritePicture(picture.public_id, isFavorite, () -> {});
        });
    }
}
