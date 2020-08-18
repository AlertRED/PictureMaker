package com.example.picturemaker.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;

import com.example.picturemaker.support.Function2;
import com.google.firebase.auth.FirebaseUser;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Storage {

    private static Storage instance;
    public final FirebaseDB firebase;
    public ViewPictureDao viewPictureDao;
    LocalStorage localStorage;
    InternalDB db;
    PictureDao pictureDao;

    private Storage(Context context) {
        this.firebase = FirebaseDB.getInstance(context);
        this.localStorage = new LocalStorage();
        this.db = InternalDB.getDatabase(context);
        this.pictureDao = this.db.pictureDao();
        this.viewPictureDao = this.db.viewPictureDao();
    }

    public static Storage getInstance(Context context) {
        if (instance == null) {
            instance = new Storage(context);
        }
        return instance;
    }

    public FirebaseUser getUser(){
        return this.firebase.getUser();
    }

    public void GetGenres(Consumer<List<String>> foo) {
        List<String> genres = this.localStorage.GetGenres();
        if (genres != null) {
            foo.accept(genres);
        } else {
            this.firebase.loadGenres(genres1 -> {
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
            this.firebase.loadAuthors(authors1 -> {
                this.localStorage.SaveStorageAuthors(authors1);
                GetAuthors(foo);
            });
        }
    }

    public LiveData<List<Picture>> GetLiveDataFromView(String viewName) {
        return this.viewPictureDao.getPicturesFromView(viewName);
    }

    public void LoadPicturesByGallery(Context context, Map<String, Object> parameters) {
        this.DeletePictures("Gallery");
        this.LoadPictures(context,"Gallery", parameters);
    }

    public void LoadPicturesByCollection(Context context) {
        Map<String, Object> parameters = new Hashtable<>();
        this.LoadPictures(context,"Collection", parameters);
    }

    public void LoadPicturesByTop(Context context) {
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("is_popular", true);
        this.LoadPictures(context, "Top", parameters);
    }

    public void LoadPicturesByPopular(Context context) {
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("is_popular", true);
        this.LoadPictures(context, "Popular", parameters);
    }

    public LiveData<Picture> GetLivePicture(long id) {
        return this.pictureDao.liveById(id);
    }

    private void DeletePictures(String viewName) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> this.viewPictureDao.deleteAllPicturesFromView(viewName));
    }

    private void LoadPictures(Context context, String viewName, Map<String, Object> parameters) {
        this.firebase.LoadPictures(pictures -> {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                for (Picture picture : pictures) {
                        long id = this.pictureDao.insertOrUpdate(picture);
                        this.viewPictureDao.insert(new ViewPicture(viewName, id));
                }
            });
        }, parameters);
    }

    public void GetImage(Context context, String picture_name, ImageView imageView) {
        this.firebase.loadImage(context, picture_name, imageView, false);
    }

    public void SetFavoritePicture(long pictureId, boolean isFavorite) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            Picture picture = pictureDao.findById(pictureId);
            picture.is_favorite = isFavorite;
            pictureDao.update(picture);
            this.firebase.SetFavoritePicture(picture.public_id, isFavorite, () -> {
            });
        });
    }
}
